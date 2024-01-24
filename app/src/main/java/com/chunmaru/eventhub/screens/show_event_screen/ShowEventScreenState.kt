package com.chunmaru.eventhub.screens.show_event_screen

import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event

sealed class ShowEventScreenState {

    object Initial : ShowEventScreenState()

    object PendingDate : ShowEventScreenState()

    data class ShowEvent(
        val author: Author,
        val event: Event,
        val favorite: Boolean,
        val isHeld: Boolean,
    ) : ShowEventScreenState()

}