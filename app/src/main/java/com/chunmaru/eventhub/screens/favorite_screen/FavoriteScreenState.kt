package com.chunmaru.eventhub.screens.favorite_screen

import com.chunmaru.eventhub.data.model.Event

sealed class FavoriteScreenState {

    object Initial : FavoriteScreenState()

    object PendingResult : FavoriteScreenState()

    data class ShowData(
        val eventList: List<Event>
    ) : FavoriteScreenState()


}