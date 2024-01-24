package com.chunmaru.eventhub.data.repositories

import android.util.Log
import com.chunmaru.eventhub.data.FirebaseKnot
import com.chunmaru.eventhub.data.model.DeletedEventInfo
import com.chunmaru.eventhub.data.model.FireBaseDeletedEventInfo
import com.chunmaru.eventhub.domain.DeletedEventsMessageRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class DeletedEventsMessageRepositoryImpl : DeletedEventsMessageRepository {

    private val database =
        Firebase.database.getReference(FirebaseKnot.DELETED_EVENTS_MESSAGE)

    override suspend fun addDeletedEvent(deletedEventInfo: DeletedEventInfo): Boolean {
        Log.d("MyTag", "addDeletedEvent: $deletedEventInfo ")
        return try {
            val eventRef = database.child(deletedEventInfo.eventId)
            val eventInfoMap = mapOf(
                "eventId" to deletedEventInfo.eventId,
                "eventName" to deletedEventInfo.eventName,
                "authorId" to deletedEventInfo.authorId,
                "reason" to deletedEventInfo.reason
            )

            eventRef.updateChildren(eventInfoMap).await()
            Log.d("MyTag", "addDeletedEvent: success ")
            true
        } catch (e: Exception) {
            Log.d("MyTag", "addDeletedEvent: $e ")
            false
        }
    }


    override suspend fun getDeletedEventsByAuthor(eventIds: List<String>): List<DeletedEventInfo> =
        withContext(NonCancellable)
        {
            try {
                Log.d("MyTag", "getDeletedEventsByAuthor: ${Firebase.auth.uid!!}")
                val eventsList = mutableListOf<DeletedEventInfo>()
                eventIds.forEach { id ->
                    val query =
                        Firebase.database.getReference("Message Deleted Events")
                            .child(id).get().await()

                    val eventInfo = query.getValue(FireBaseDeletedEventInfo::class.java)
                    Log.d("MyTag", "getDeletedEventsByAuthor: $eventInfo ")
                    eventInfo?.let { eventsList.add(it.toDeletedEventInfo()) }
                }
                eventsList
            } catch (e: Exception) {
                Log.d("MyTag", "Error getting deleted events by author: $e")
                emptyList()
            }
        }
//            val valueEventListener = object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    try {
//                        val eventsList = mutableListOf<DeletedEventInfo>()
//                        for (eventSnapshot in dataSnapshot.children) {
//                            val eventInfo =
//                                eventSnapshot.getValue(FireBaseDeletedEventInfo::class.java)
//                            eventInfo?.let { eventsList.add(it.toDeletedEventInfo()) }
//                        }
//
//                        trySend(eventsList)
//                    } catch (e: Exception) {
//                        Log.d("MyTag", "Error getting deleted events by author: $e")
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    Log.d("MyTag", "Database error: $databaseError")
//                    trySend(emptyList())
//                }
//            }

//            query.addValueEventListener(valueEventListener)
//            awaitClose {
//                query.addValueEventListener(valueEventListener)
//            }


    override suspend fun deleteEventMessageById(eventId: String) {
        try {
            val eventRef = database.child(eventId)
            eventRef.removeValue().await()
        } catch (e: Exception) {
            Log.d("MyTag", "deleteEventMessageById: $e ")
            throw e
        }
    }

    override suspend fun deleteAllEventMessagesForAuthor(eventIds: List<String>): Boolean {

        Log.d("MyTag", "deleteAllEventMessagesForAuthor: $eventIds ")
        return try {

            eventIds.forEach { id ->
                Firebase.database.getReference("Message Deleted Events")
                    .child(id).removeValue().await()
            }

            Log.d("MyTag", "deleteAllEventMessagesForAuthor: success ")
            true
        } catch (e: Exception) {
            Log.d("MyTag", "deleteAllEventMessagesForAuthor: $e ")
            false
        }

    }


}