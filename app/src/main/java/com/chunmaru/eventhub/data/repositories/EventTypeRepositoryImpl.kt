package com.chunmaru.eventhub.data.repositories

import android.util.Log
import com.chunmaru.eventhub.data.FirebaseKnot
import com.chunmaru.eventhub.domain.EventTypeRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class EventTypeRepositoryImpl : EventTypeRepository {

    private val dataBase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val eventTypeReference = dataBase.getReference(FirebaseKnot.EVENT_TYPE)

    override fun getAllType(): Flow<List<String>> =  callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val types = dataSnapshot.children.mapNotNull { it.getValue(String::class.java) }
                trySend(types)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("MyTag", "Помилка: ${databaseError.message}")
                close(databaseError.toException())
            }
        }

        eventTypeReference.addValueEventListener(valueEventListener)

        awaitClose { eventTypeReference.removeEventListener(valueEventListener) }
    }

    override suspend fun addType(type: String) = suspendCoroutine<Boolean> { continuation ->

        val newTypeReference = eventTypeReference.push()

        newTypeReference.setValue(type).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(true)
            } else {
                val exception = task.exception
                continuation.resumeWithException(
                    exception ?: Exception("Помилка додавання типу події")
                )
            }
        }
    }

    override suspend fun deleteType(type: String) = suspendCoroutine { continuation ->
        eventTypeReference.orderByValue().equalTo(type)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.children.count() == 0) {
                        continuation.resume(false)
                    } else {
                        val typeSnapshot = dataSnapshot.children.first()
                        typeSnapshot.ref.removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(true)
                            } else {
                                val exception = task.exception
                                continuation.resumeWithException(
                                    exception ?: Exception("Помилка видалення типу події")
                                )
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    val exception = databaseError.toException()
                    continuation.resumeWithException(exception)
                }
            })
    }


}