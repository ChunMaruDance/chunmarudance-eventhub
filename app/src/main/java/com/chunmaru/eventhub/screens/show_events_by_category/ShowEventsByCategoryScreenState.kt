package com.chunmaru.eventhub.screens.show_events_by_category

import com.chunmaru.eventhub.data.model.EventsCategories


sealed class ShowEventsByCategoryScreenState {

    object Initial : ShowEventsByCategoryScreenState()

    object Pending : ShowEventsByCategoryScreenState()

    data class ShowEvents(
       val events: EventsCategories
    ):ShowEventsByCategoryScreenState()

}