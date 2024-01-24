package com.chunmaru.eventhub.presentation.screens.search_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.optional.MenuItem
import com.chunmaru.eventhub.domain.repositories.AuthorRepository
import com.chunmaru.eventhub.domain.repositories.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authorRepository: AuthorRepository,
) : ViewModel() {

    private var _combinedState: MutableStateFlow<SearchScreenCombineState> =
        MutableStateFlow(
            SearchScreenCombineState(
                SearchScreenState.Initial,
                "",
                MenuItem.DEFAULT_ITEMS_TO_SEARCH
            )
        )

    val combinedState = _combinedState.asStateFlow()

    fun search(query: String) {
        _combinedState.value = _combinedState.value.copy(searchText = query)
        if (query.length > 3) {
            performSearch(query)
        } else if (query.isEmpty()) {
            _combinedState.value = _combinedState.value.copy(
                searchScreenState = SearchScreenState.SearchResult(
                    listOf(),
                    listOf()
                )
            )
        }
    }

    fun setCriterion(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _combinedState.value = _combinedState.value.copy(
                searchCriteria = _combinedState.value.searchCriteria.map { menuItem ->
                    if (menuItem.title == title) {
                        menuItem.copy(isSelect = !menuItem.isSelect)
                    } else {
                        menuItem
                    }
                }
            )
            performSearch(_combinedState.value.searchText)
        }
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            _combinedState.value =
                _combinedState.value.copy(searchScreenState = SearchScreenState.Pending)

            val selectedCriteria = _combinedState.value.searchCriteria.filter { it.isSelect }

            val events = selectedCriteria.find { it.title == "Event name" }?.let {
                if (it.isSelect) eventRepository.searchEvent(query) else emptyList()
            } ?: emptyList()

            val authors = selectedCriteria.find { it.title == "Author name" }?.let {
                if (it.isSelect) authorRepository.searchAuthor(query) else emptyList()
            } ?: emptyList()

            val eventsByCity = selectedCriteria.find { it.title == "City name" }?.let {
                if (it.isSelect) eventRepository.searchEventsByCity(query) else emptyList()
            } ?: emptyList()

            val allEvents = events + eventsByCity

            _combinedState.value = _combinedState.value.copy(
                searchScreenState = SearchScreenState.SearchResult(
                    authors = authors,
                    events = allEvents
                )
            )

            Log.d("MyTag", "performSearch: $eventsByCity ")
            Log.d("MyLog", "performSearch: $events : $authors ")
        }
    }
}



