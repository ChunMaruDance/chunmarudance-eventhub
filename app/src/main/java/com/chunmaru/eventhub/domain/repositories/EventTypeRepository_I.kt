package com.chunmaru.eventhub.domain.repositories

import kotlinx.coroutines.flow.Flow

interface EventTypeRepository {

    fun getAllType(): Flow<List<String>>

    suspend fun addType(type:String):Boolean

    suspend fun deleteType(type: String):Boolean

}