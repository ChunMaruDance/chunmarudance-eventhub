package com.chunmaru.eventhub.screens.profile_screen

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