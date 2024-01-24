package com.chunmaru.eventhub.data.repositories

import android.util.Log
import com.chunmaru.eventhub.data.FirebaseKnot
import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.CategoryEventCount
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.event.EventAuthorType
import com.chunmaru.eventhub.data.model.event.EventsCategories
import com.chunmaru.eventhub.data.model.event.FireBaseEvent
import com.chunmaru.eventhub.domain.repositories.EventRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class EventRepositoryImpl : EventRepository {

    private val database = Firebase.database.reference.child(FirebaseKnot.EVENT)
    private val categoriesDatabase = Firebase.database.reference.child(FirebaseKnot.EVENT_CATEGORY)


    override suspend fun getEventCountsInCategories(categories: List<String>): List<CategoryEventCount> {
        val categoryEventCounts = mutableListOf<CategoryEventCount>()

        try {
            for (category in categories) {
                val categoryRef = categoriesDatabase.child(category)
                val categorySnapshot = categoryRef.get().await()

                if (categorySnapshot.exists()) {
                    val eventCount = categorySnapshot.childrenCount.toInt()
                    categoryEventCounts.add(CategoryEventCount(category, eventCount))
                } else {
                    categoryEventCounts.add(CategoryEventCount(category, 0))
                }
            }
        } catch (e: Exception) {
            Log.d("MyTag", "Error counting events in categories: $e")
        }

        return categoryEventCounts
    }


    override fun getEventByAuthorId(authorId: String): Flow<List<Event>> = callbackFlow {
        val query = database.orderByChild("authorId").equalTo(authorId)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val eventsList = mutableListOf<Event>()
                    for (eventSnapshot in dataSnapshot.children) {
                        val event = eventSnapshot.getValue(FireBaseEvent::class.java)
                        if (event != null) {
                            eventsList.add(event.toEvent())
                        }
                    }
                    trySend(eventsList)
                } catch (e: Exception) {
                    Log.d("MyTag", "Помилка отримання даних з бази даних $e")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("MyTag", "Помилка бази даних: $databaseError")
                close(databaseError.toException())
            }
        }

        query.addValueEventListener(valueEventListener)

        awaitClose {
            query.removeEventListener(valueEventListener)
        }
    }

    override fun getFirst7ByCategories(categories: List<String>): Flow<List<EventsCategories>> =
        callbackFlow {
            val authorRepository = AuthorRepositoryImpl()
            val query = categoriesDatabase

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val result = mutableListOf<EventsCategories>()

                        for (category in categories) {
                            val eventsList = mutableListOf<EventAuthorType>()
                            val categorySnapshot = dataSnapshot.child(category)

                            for (eventSnapshot in categorySnapshot.children) {

                                if (eventsList.size >= 7) break

                                val eventId = eventSnapshot.key
                                if (eventId != null) {
                                    launch {
                                        val eventDeferred =
                                            async { getEventByEventId(eventId) }
                                        val authorDeferred = async {
                                            authorRepository.getAuthorByEventId(eventId)
                                        }

                                        val event = eventDeferred.await()
                                        val author = authorDeferred.await()
                                        if (author != null && event != null) {
                                            val eventAuthorType = EventAuthorType(event, author)
                                            eventsList.add(eventAuthorType)
                                        }

                                    }

                                }
                            }

                            val eventsCategories = EventsCategories(category, eventsList)
                            result.add(eventsCategories)
                        }

                        trySend(result)
                    } catch (e: Exception) {
                        Log.d("MyTag", "Помилка отримання даних з бази даних: $e")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("MyTag", "Помилка бази даних: $databaseError")
                    close(databaseError.toException())
                }
            }

            query.addValueEventListener(valueEventListener)

            awaitClose {
                query.removeEventListener(valueEventListener)
            }
        }

    override suspend fun getEventByEventId(eventId: String): Event? {
        return try {
            val snapshot = database.child(eventId).get().await()
            val fireBaseEvent = snapshot.getValue(FireBaseEvent::class.java)
            fireBaseEvent?.toEvent()
                ?: throw NoSuchElementException("Event with ID $eventId not found")
        } catch (e: Exception) {
            Log.d("MyTag", "Error getting event by ID: $e")
            null
        }
    }


    override suspend fun getFavoriteEvents(author: Author): List<Event> {
        val eventIds = author.likedEvents
        val authorRepository = AuthorRepositoryImpl()
        return try {
            val eventsList = mutableListOf<Event>()

            for (eventId in eventIds) {
                val query = database.orderByChild("id").equalTo(eventId)
                val dataSnapshot = query.get().await()

                if (dataSnapshot.exists()) {
                    val event = dataSnapshot.children.first().getValue(FireBaseEvent::class.java)
                    if (event != null) {
                        eventsList.add(event.toEvent())
                    } else {
                        Log.d("MyTag", "getFavoriteEvents: ${event ?: 1} ")
                        authorRepository.removeLikeEvent(eventId)
                        dataSnapshot.ref.removeValue().await()
                    }
                } else {
                    Log.d("MyTag", "getFavoriteEvents: $eventId ")
                    authorRepository.removeLikeEvent(eventId)
                }
            }

            Log.d("MyTag", "loadFavoriteEvents: $eventsList")
            eventsList
        } catch (e: Exception) {
            Log.d("MyTag", "Помилка отримання даних з бази даних $e")
            emptyList()
        }
    }


    override suspend fun createEvent(event: Event): String {

        Log.d("MyTag", "createEvent 12121: $event ")

        try {
            val eventId = database.push().key
            if (eventId != null) {
                val eventWithId = event.copy(id = eventId)
                database.child(eventId).setValue(eventWithId).await()

                val categories = event.categories

                for (category in categories) {
                    Log.d("MyTag", "createEvent 12121: $category ")
                    val categoryRef = categoriesDatabase.child(category)
                    val categorySnapshot = categoryRef.get().await()

                    if (categorySnapshot.exists()) {
                        val eventsList = mutableListOf<String>()
                        for (eventSnapshot in categorySnapshot.children) {
                            Log.d("MyTag", "createEvent exist: $category ")
                            eventsList.add(eventSnapshot.key ?: "")
                        }
                        categoryRef.child(eventId).setValue(true)

                    } else {
                        categoryRef.child(eventId).setValue(true)
                    }
                }

                return eventId
            } else {
                throw IllegalStateException("Failed to generate event ID")
            }
        } catch (e: Exception) {
            Log.d("MyTag", "Error creating event: $e")
            throw e
        }
    }

    override suspend fun updateEvent(event: Event) {
        Log.d("MyTag", "updateEvent: $event ")
        try {
            val eventId = event.id
            val oldCategories = getEventByEventId(eventId)!!.categories

            database.child(eventId).setValue(event).await()

            for (oldCategory in oldCategories) {
                if (!event.categories.contains(oldCategory)) {
                    val oldCategoryRef =
                        categoriesDatabase.child(oldCategory)
                    oldCategoryRef.child(eventId).removeValue().await()
                }
            }

            for (category in event.categories) {
                val categoryRef = categoriesDatabase.child(category)
                categoryRef.child(eventId).setValue(true).await()
            }
        } catch (e: Exception) {
            Log.d("MyTag", "Error updating event: $e")
            throw e
        }
    }


    override suspend fun searchEvent(query: String): List<Event> {
        return try {
            val result = mutableListOf<Event>()

            val queryResult = database.orderByChild("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .await()

            for (dataSnapshot in queryResult.children) {
                val fireBaseEvent = dataSnapshot.getValue(FireBaseEvent::class.java)
                if (fireBaseEvent != null) {
                    result.add(fireBaseEvent.toEvent())
                }
            }

            result
        } catch (e: Exception) {
            Log.d("MyTag", "Помилка отримання даних з бази даних: $e")
            emptyList()
        }
    }

    override suspend fun getAllEventsByCategory(category: String): EventsCategories {
        val result = mutableListOf<EventAuthorType>()

        try {
            val query = categoriesDatabase.child(category).orderByKey()
            val dataSnapshot = query.get().await()

            for (eventSnapshot in dataSnapshot.children) {
                val eventId = eventSnapshot.key
                if (eventId != null) {
                    val event = getEventByEventId(eventId)
                    val author = AuthorRepositoryImpl().getAuthorByEventId(eventId)
                    if (author != null && event != null) {
                        val eventAuthorType = EventAuthorType(event, author)
                        result.add(eventAuthorType)
                    }

                }
            }
        } catch (e: Exception) {
            Log.d("MyTag", "Error getting all events by category: $e")
        }
        return EventsCategories(category, result)
    }

    override suspend fun deleteEvent(event: Event): Boolean {
        return try {
            val eventCategoriesRef = categoriesDatabase

            val categoriesQuery = eventCategoriesRef.orderByKey()
            val categoriesSnapshot = categoriesQuery.get().await()

            for (categorySnapshot in categoriesSnapshot.children) {
                val category = categorySnapshot.key ?: continue

                val eventInCategoryRef = eventCategoriesRef.child(category).child(event.id)
                eventInCategoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            eventInCategoryRef.removeValue().addOnCompleteListener {
                                if (!it.isSuccessful) {
                                    Log.d(
                                        "MyTag",
                                        "Помилка видалення івента з категорії $category: ${it.exception}"
                                    )
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(
                            "MyTag",
                            "Помилка видалення івента з категорії $category: ${error.message}"
                        )
                    }
                })
            }

            val storageRef = Firebase.storage.reference.child("images/${event.imgUri.path}")
            storageRef.delete().await()

            database.child(event.id).removeValue().await()

            true
        } catch (e: CancellationException) {
            Log.d("MyTag", "Корутина була скасована: $e")
            false
        } catch (e: Exception) {
            Log.d("MyTag", "Помилка видалення події: $e")
            false
        }
    }

    override suspend fun searchEventsByCity(city: String): List<Event> {
        return try {
            val query = database.orderByChild("city")
                .startAt(city)
                .endAt(city + "\uf8ff")

            val eventsList = mutableListOf<Event>()
            val dataSnapshot = query.get().await()

            for (eventSnapshot in dataSnapshot.children) {
                val event = eventSnapshot.getValue(FireBaseEvent::class.java)
                if (event != null) {
                    eventsList.add(event.toEvent())
                }
            }

            eventsList
        } catch (e: Exception) {
            Log.d("MyTag", "Error getting events by city prefix: $e")
            emptyList()
        }
    }

    override suspend fun deleteCategory(category: String): Boolean {
        return try {

            val eventIds = categoriesDatabase.child(category).get().await().children.mapNotNull {
                it.key
            }

            eventIds.forEach { eventId ->
                deleteCategoryFromEvent(eventId, category)
            }

            categoriesDatabase.child(category).removeValue().await()
            true
        } catch (e: Exception) {
            Log.d("MyTag", "Помилка видалення категорії: $e")
            false
        }
    }

    override suspend fun getEventName(id: String): String = try {
        val nameSnapshot = database.child(id).child("name").get().await()
        nameSnapshot.getValue(String::class.java) ?: ""
    } catch (e: Exception) {
        Log.d("MyTag", "getEventName: $e ")
        throw e
    }


    private suspend fun deleteCategoryFromEvent(eventId: String, category: String) {
        try {
            val eventSnapshot = database.child(eventId).get().await()
            val fireBaseEvent = eventSnapshot.getValue(FireBaseEvent::class.java)
            val event = fireBaseEvent?.toEvent()

            if (event?.categories != null) {
                val updatedEvent = event.copy(categories = event.categories.toMutableList().apply {
                    remove(category)
                })
                database.child(eventId).setValue(updatedEvent).await()
            }
        } catch (e: Exception) {
            Log.d("MyTag", "Помилка видалення категорії з івента: $e")
        }
    }

}
