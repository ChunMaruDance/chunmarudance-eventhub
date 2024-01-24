package com.chunmaru.eventhub.data.model.author

import com.chunmaru.eventhub.data.model.event.Event

data class AuthorEvents(
    val author: Author,
    val authorEvents: List<Event>,
    val isAdmin: Boolean
)