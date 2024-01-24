package com.chunmaru.eventhub.presentation.screens.search_screen

import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.optional.MenuItem

sealed class SearchScreenState {

    object Initial : SearchScreenState()

    object Pending : SearchScreenState()

    data class SearchResult(
        val authors: List<Author>,
        val events: List<Event>
    ) : SearchScreenState()

}

data class SearchScreenCombineState(
    val searchScreenState: SearchScreenState,
    val searchText: String,
    val searchCriteria: List<MenuItem>
)
