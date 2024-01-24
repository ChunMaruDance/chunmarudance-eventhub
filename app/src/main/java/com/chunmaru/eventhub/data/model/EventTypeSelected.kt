package com.chunmaru.eventhub.data.model


data class EventTypeSelected(
    val selected: Boolean,
    val eventsCategories: EventsCategories
)

data class EventsCategories(
    val category: String,
    val events: List<EventAuthorType>
)

data class EventAuthorType(
    val event: Event,
    val author: Author
)

data class EventType(
    val type: String,
    val selected: Boolean
)

data class CategoryEventCount(val category: String, val eventCount: Int)
data class AuthorEventCount(val authorName: String, val eventCount: Int)

data class EventReviewCount(val eventName: String, val reviewCount: Int)

