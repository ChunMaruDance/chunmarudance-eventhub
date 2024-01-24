package com.chunmaru.eventhub.screens.home_screen

import com.chunmaru.eventhub.data.model.EventTypeSelected

sealed class HomeScreenState {

    object Initial : HomeScreenState()

    object PendingData : HomeScreenState()

    data class ShowData(
        val events: List<EventTypeSelected>
    ) : HomeScreenState()

}