package com.chunmaru.eventhub.data.repositories


import android.util.Log
import com.chunmaru.eventhub.data.FirebaseKnot
import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.AuthorEventCount
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.data.model.FireBaseAuthor
import com.chunmaru.eventhub.data.model.ImageData
import com.chunmaru.eventhub.domain.AuthorRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import kotlinx.coroutines.tasks.await

class AuthorRepositoryImpl : AuthorRepository {

    private val authorsReference = Firebase.database.getReference(FirebaseKnot.AUTHOR)
    private val uid
        get() = Firebase.auth.currentUser?.uid

    override suspend fun getAuthorsEventCounts(): List<AuthorEventCount> {
        val authorEventCounts = mutableListOf<AuthorEventCount>()
        try {
            val allAuthorsSnapshot = authorsReference.get().await()
            for (authorSnapshot in allAuthorsSnapshot.children) {
                val firebaseAuthor = authorSnapshot.getValue(FireBaseAuthor::class.java)
                firebaseAuthor?.let {
                    val eventsCount = authorSnapshot.child("events").childrenCount.toInt()
                    val authorEventCount = AuthorEventCount(it.toAuthor().name, eventsCount)
                    authorEventCounts.add(authorEventCount)
                }
            }
        } catch (e: Exception) {
            Log.d("MyTag", "Error counting events for authors: $e")
        }

        return authorEventCounts
    }


    override suspend fun getAuthor(uid: String): Author {
        val authorSnapshot = authorsReference.child(uid).get().await()
        val firebaseAuthor = authorSnapshot.getValue(FireBaseAuthor::class.java)
        return firebaseAuthor?.toAuthor() ?: throw IllegalStateException("Author not founded")

    }

    override suspend fun deleteAuthor(uid: String): Boolean {
        return try {
            authorsReference.child(uid).removeValue().await()
            true
        } catch (e: Exception) {
            false
        }

    }

    override suspend fun createAuthor(author: FireBaseAuthor): Boolean {
        return try {
            authorsReference.child(author.id!!).setValue(author).await()
            true
        } catch (e: Exception) {
            Log.d("MyTag", "createAuthor: $e ")
            false
        }

    }

    override fun setName(newName: String): Boolean {
        Log.d("MyTag", "setName: $uid ")
        return setStringElement("name", newName)
    }

    override fun setDescription(newDescriptor: String): Boolean {
        return setStringElement("description", newDescriptor)
    }

    override fun setAvatar(newAvatar: ImageData): Boolean {
        uid ?: return false
        return try {
            authorsReference.child(uid!!).child("avatar").setValue(newAvatar)
            true
        } catch (e: Exception) {
            Log.d("MyTag", "setAvatar: $e ")
            false
        }

    }


    override suspend fun likeEvent(eventId: String): Boolean {
        return setListElement("likedEvents", eventId)
    }

    override suspend fun removeLikeEvent(eventId: String): Boolean {
        Log.d("MyTag", "removeLikeEvent: $eventId ")
        return removeListElement("likedEvents", eventId)
    }

    override suspend fun addToDeletedId(eventId: String, authorId: String): Boolean {
        return setListElementWithId("deletedIds", eventId, authorId)
    }

    override suspend fun deleteDeletedId(eventId: String, authorId: String): Boolean {
        return removeListElementWithId("deletedIds", eventId, authorId)
    }

    override suspend fun deleteDeletedIds(): Boolean {
        return try {
            uid?.let { authorId ->
                val deletedIdsRef = authorsReference.child(authorId).child("deletedIds")
                deletedIdsRef.removeValue().await()
                true
            } ?: false
        } catch (e: Exception) {
            Log.d("MyTag", "Error deleting deletedIds: $e")
            false
        }
    }

    override suspend fun selectEvent(eventId: String): Boolean {
        return setListElement("selectedEvents", "eventId")
    }

    override suspend fun addEvent(eventId: String): Boolean {
        return setListElement("events", eventId)
    }

    override suspend fun deleteEvent(event: Event): Boolean {
        uid ?: return false
        return try {
            removeListElementWithId("events", eventId = event.id, id = event.authorId)
            removeListElementWithId("likedEvents", eventId = event.id, id = event.authorId)
            removeListElement("selectedEvents", event.id)
            true
        } catch (e: Exception) {
            Log.d("MyTag", "Error deleting event: $e")
            false
        }
    }


    override suspend fun getAuthorByEventId(eventId: String): Author? {
        Log.d("MyTag", "getAuthorByEventId: $eventId ")
        return try {
            val eventSnapshot =
                FirebaseDatabase.getInstance().getReference(FirebaseKnot.EVENT).child(eventId).get()
                    .await()
            if (eventSnapshot.exists()) {
                val authorId = eventSnapshot.child("authorId").getValue(String::class.java)

                if (authorId != null) {
                    return getAuthor(authorId)
                } else null

            } else null

        } catch (e: Exception) {
            Log.d("MyTag", "Error getting author by event ID: $e")
            null
        }
    }

    override fun signOut(): Boolean {
        return try {
            FirebaseAuth.getInstance().signOut()
            true
        } catch (e: Exception) {
            false
        }
    }


    override suspend fun searchAuthor(query: String): List<Author> {
        return try {
            val result = mutableListOf<Author>()

            val queryResult = authorsReference.orderByChild("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .await()

            for (dataSnapshot in queryResult.children) {
                val firebaseAuthor = dataSnapshot.getValue(FireBaseAuthor::class.java)
                if (firebaseAuthor != null) {
                    result.add(firebaseAuthor.toAuthor())
                }
            }

            result
        } catch (e: Exception) {

            emptyList()
        }
    }

    override suspend fun hasLikedEvent(eventId: String): Boolean {
        Log.d("MyTag", "hasLikedEvent: ${Firebase.auth.currentUser?.uid}")
        val uid = Firebase.auth.currentUser?.uid ?: return false
        return try {
            val snapshot =
                authorsReference.child(uid).child("likedEvents").child(eventId).get().await()
            snapshot.exists()
        } catch (e: Exception) {
            Log.d("MyTag", "Error checking if the event is liked: $e")
            false
        }
    }


    private fun setStringElement(type: String, value: String): Boolean {
        uid ?: return false
        Log.d("MyTag", "setListElement $type: $value ")
        return try {
            authorsReference.child(uid!!).child(type).setValue(value)
            true
        } catch (e: Exception) {
            Log.d("MyTag", "setString $type: $e ")
            false
        }

    }

    private suspend fun removeListElement(type: String, eventId: String): Boolean {
        uid ?: return false
        return try {
            val eventRef = authorsReference.child(uid!!).child(type).child(eventId)
            val snapshot = eventRef.get().await()
            Log.d("MyTag", "removeListElement: $snapshot ")
            if (snapshot.exists()) {
                eventRef.removeValue().await()
                Log.d("MyTag", "Successfully removed element with ID: $eventId")
                true
            } else {
                Log.d("MyTag", "Element with ID $eventId does not exist")
                false
            }
        } catch (e: Exception) {
            Log.d("MyTag", "Error removing list element: $e")
            false
        }
    }

    private suspend fun setListElement(type: String, eventId: String): Boolean {
        uid ?: return false
        return try {
            val ref = authorsReference.child(uid!!).child(type).child(eventId)
            ref.setValue(eventId).await()
            true
        } catch (e: Exception) {
            Log.d("MyTag", "setListElement $type: $e ")
            false
        }

    }


    private suspend fun setListElementWithId(type: String, eventId: String, id: String): Boolean {
        return try {
            val ref = authorsReference.child(id).child(type).child(eventId)
            ref.setValue(eventId).await()
            true
        } catch (e: Exception) {
            Log.d("MyTag", "setListElementWithId: $e\n&type:$type\neventId:$eventId\nid:$id ")
            false
        }

    }

    private suspend fun removeListElementWithId(
        type: String,
        eventId: String,
        id: String
    ): Boolean {
        return try {
            val eventRef = authorsReference.child(id).child(type).child(eventId)
            val snapshot = eventRef.get().await()
            Log.d("MyTag", "removeListElement: $snapshot ")
            if (snapshot.exists()) {
                eventRef.removeValue().await()
                Log.d("MyTag", "Successfully removed element with ID: $eventId")
                true
            } else {
                Log.d("MyTag", "Element with ID $eventId does not exist")
                false
            }
        } catch (e: Exception) {
            Log.d("MyTag", "Error removing list element: $e")
            false
        }
    }

}