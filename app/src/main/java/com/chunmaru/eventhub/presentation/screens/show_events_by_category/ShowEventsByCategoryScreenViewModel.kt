package com.chunmaru.eventhub.presentation.screens.show_events_by_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.event.EventsCategories
import com.chunmaru.eventhub.domain.repositories.AuthorRepository
import com.chunmaru.eventhub.domain.repositories.EventRepository
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowEventsByCategoryScreenViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authorRepository: AuthorRepository
) : ViewModel() {

    private var _state: MutableStateFlow<ScreenState<EventsCategories>> =
        MutableStateFlow(ScreenState.Initial())
    val state = _state.asStateFlow()

    private val _category = MutableStateFlow("Other")
    private var likedEvents = setOf<String>()

    fun setCategory(category: String) {
        _category.value = category
        getEventsByCategory()
    }

    private fun getEventsByCategory() {
        _state.value = ScreenState.Pending()
        viewModelScope.launch(Dispatchers.IO) {
            likedEvents = authorRepository.getAuthor(Firebase.auth.uid!!).likedEvents.map {
                it
            }.toSet()
            val events = eventRepository.getAllEventsByCategory(_category.value)
            _state.value = ScreenState.Success(events)
        }
    }

    fun likeEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!likedEvents.contains(event.id))
                authorRepository.likeEvent(event.id)
        }

    }

}