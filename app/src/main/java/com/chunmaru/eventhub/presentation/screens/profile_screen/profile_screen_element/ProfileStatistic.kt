package com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element

sealed class ProfileStatistic(
    val count: String,
    val title: String
) {

    data class LikeStatistic(
        val counts: String
    ) : ProfileStatistic(
        count = counts,
        title = LIKE
    )

    data class EventsStatistic(
        val counts: String
    ) : ProfileStatistic(
        count = counts,
        title = EVENTS
    )

    data class FatalStatistic(
        val counts: String
    ) : ProfileStatistic(
        count = counts,
        title = FATAL
    )

    companion object {
        const val LIKE = "Like"
        const val EVENTS = "Events"
        const val FATAL = "Fatal"

    }


}