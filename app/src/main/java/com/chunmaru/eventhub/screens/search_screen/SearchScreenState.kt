package com.chunmaru.eventhub.screens.search_screen

import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.data.model.MenuItem

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
