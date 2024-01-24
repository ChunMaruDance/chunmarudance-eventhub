package com.chunmaru.eventhub.presentation.screens.admin_screen

import com.chunmaru.eventhub.data.model.event.AuthorEventCount
import com.chunmaru.eventhub.data.model.event.CategoryEventCount
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.event.EventReviewCount
import com.chunmaru.eventhub.data.model.event.EventType

sealed class AdminScreenState {
    object Initial : AdminScreenState()
    object Pending : AdminScreenState()
    data class ShowData(
        val events: List<Event>,
        val types: List<EventType>,
        val showStatistic: ShowStatisticsCategory,
        val showActiveAuthors: ShowActiveAuthors,
        val showReviewsStatistics: ShowReviewsStatistics
    ) : AdminScreenState()
}

sealed class ShowStatisticsCategory {
    object Initial : ShowStatisticsCategory()
    object Pending : ShowStatisticsCategory()
    data class ShowStatistics(
        val popularCategory: List<CategoryEventCount>,
        val unpopularCategory: List<CategoryEventCount>
    ) : ShowStatisticsCategory()

}
sealed class ShowActiveAuthors {

    object Initial : ShowActiveAuthors()

    object Pending : ShowActiveAuthors()

    data class ShowAuthors(
        val activeAuthor: List<AuthorEventCount>
    ) : ShowActiveAuthors()

}

sealed class ShowReviewsStatistics {
    object Initial : ShowReviewsStatistics()
    object Pending : ShowReviewsStatistics()
    data class ShowReviews(
        val reviews: List<EventReviewCount>
    ) : ShowReviewsStatistics()

}




