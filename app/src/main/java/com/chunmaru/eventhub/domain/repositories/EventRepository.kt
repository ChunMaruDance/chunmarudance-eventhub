package com.chunmaru.eventhub.domain.repositories

import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.CategoryEventCount
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.event.EventsCategories
import kotlinx.coroutines.flow.Flow

interface EventRepository {


    suspend fun getEventCountsInCategories(categories: List<String>): List<CategoryEventCount>

    fun getEventByAuthorId(authorId: String): Flow<List<Event>>

    suspend fun getEventByEventId(eventId: String): Event?

    suspend fun getFavoriteEvents(author: Author): List<Event>

    suspend fun createEvent(event: Event): String

    suspend fun updateEvent(event: Event)

    fun getFirst7ByCategories(categories: List<String>): Flow<List<EventsCategories>>

    //  suspend fun getAllEventByCategories(category: String): List<Event>

    suspend fun searchEvent(query: String): List<Event>

    suspend fun getAllEventsByCategory(category: String): EventsCategories

    suspend fun deleteEvent(event: Event): Boolean

    suspend fun searchEventsByCity(city: String): List<Event>

    suspend fun deleteCategory(category: String): Boolean

    suspend fun getEventName(id: String): String


}