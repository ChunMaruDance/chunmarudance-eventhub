package com.chunmaru.eventhub.screens.profile_screen

import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event

sealed class ProfileScreenState {

    object Initial : ProfileScreenState()

    object PendingResult : ProfileScreenState()

    class ShowProfile(
        val author: Author,
        val authorEvents: List<Event>,
        val isAdmin:Boolean
    ) : ProfileScreenState()

}