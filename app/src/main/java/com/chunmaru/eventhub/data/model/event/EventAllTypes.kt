package com.chunmaru.eventhub.data.model.event

data class EventAllTypes(
    val event: Event,
    val allTypes: List<EventType>,
)