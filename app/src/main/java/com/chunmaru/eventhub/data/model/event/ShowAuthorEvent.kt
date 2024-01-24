package com.chunmaru.eventhub.data.model.event

import com.chunmaru.eventhub.data.model.author.Author

data class ShowAuthorEvent(
    val author: Author,
    val event: Event,
    val favorite: Boolean,
    val isHeld: Boolean,
)