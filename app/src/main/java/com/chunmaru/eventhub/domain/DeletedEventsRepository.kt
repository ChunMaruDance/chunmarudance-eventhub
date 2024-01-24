package com.chunmaru.eventhub.domain

import com.chunmaru.eventhub.data.model.DeletedEventInfo
import kotlinx.coroutines.flow.Flow

interface DeletedEventsMessageRepository {

    suspend fun addDeletedEvent(deletedEventInfo: DeletedEventInfo): Boolean

    suspend fun getDeletedEventsByAuthor(eventIds: List<String>): List<DeletedEventInfo>

    suspend fun deleteEventMessageById(eventId: String)

    suspend fun deleteAllEventMessagesForAuthor(eventIds: List<String>): Boolean
}