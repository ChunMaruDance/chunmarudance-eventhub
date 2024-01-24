package com.chunmaru.eventhub.data.repositories

import com.chunmaru.eventhub.data.FirebaseKnot
import com.chunmaru.eventhub.domain.AdminsRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AdminsRepositoryImpl : AdminsRepository {
    private val fireBaseAuth
        get() = Firebase.auth.currentUser?.uid
    private val databaseReference = Firebase.database.getReference(FirebaseKnot.ADMINS)
    override suspend fun isCurrentUserAdmin(): Boolean {
        val currentUserUid = fireBaseAuth ?: return false
        return try {
            val adminsSnapshot = databaseReference.child(currentUserUid).get().await()
            val res = adminsSnapshot.getValue(Boolean::class.java)
            res ?: false
        } catch (e: Exception) {
            false
        }
    }
}