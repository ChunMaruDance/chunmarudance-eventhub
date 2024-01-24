package com.chunmaru.eventhub.screens.create_event_screen

import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.data.model.EventType
import com.chunmaru.eventhub.data.model.ReviewAuthor

sealed class CreateEventScreenState {

    object Initial : CreateEventScreenState()

    object Pending : CreateEventScreenState()

    data class CreateEvent(
        val event: Event,
        val allTypes: List<EventType>,
    ) : CreateEventScreenState()

}
