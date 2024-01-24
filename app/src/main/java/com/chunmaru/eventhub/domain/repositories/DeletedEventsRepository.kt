package com.chunmaru.eventhub.domain.repositories

import com.chunmaru.eventhub.data.model.event.DeletedEventInfo

interface DeletedEventsMessageRepository {

    suspend fun addDeletedEvent(deletedEventInfo: DeletedEventInfo): Boolean

    suspend fun getDeletedEventsByAuthor(eventIds: List<String>): List<DeletedEventInfo>

    suspend fun deleteEventMessageById(eventId: String)

    suspend fun deleteAllEventMessagesForAuthor(eventIds: List<String>): Boolean
}