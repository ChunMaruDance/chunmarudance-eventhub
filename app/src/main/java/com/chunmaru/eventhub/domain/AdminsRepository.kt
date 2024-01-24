package com.chunmaru.eventhub.domain

interface AdminsRepository {
    suspend fun isCurrentUserAdmin(): Boolean

}