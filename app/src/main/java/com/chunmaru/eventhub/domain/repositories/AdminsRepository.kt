package com.chunmaru.eventhub.domain.repositories

interface AdminsRepository {
    suspend fun isCurrentUserAdmin(): Boolean

}