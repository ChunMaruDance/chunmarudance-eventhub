package com.chunmaru.eventhub.presentation.screens.favorite_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.event.Event
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
class FavoriteScreenViewModel @Inject constructor(
    private val authorRepository: AuthorRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private var _state: MutableStateFlow<ScreenState<List<Event>>> =
        MutableStateFlow(ScreenState.Initial())
    val state = _state.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadFavorite() {
        _state.value = ScreenState.Pending()
        loadFavoriteEvents()
    }

    fun removeFromFavorite(event: Event) {
        val currentState = _state.value as? ScreenState.Success ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val events = currentState.data.toMutableList()
            events.remove(event)
            _state.value = currentState.copy(data = events)
            authorRepository.removeLikeEvent(event.id)
        }
    }

    fun loadFavoriteEvents() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ScreenState.Pending()
            val author = authorRepository.getAuthor(Firebase.auth.uid!!)
            val events = eventRepository.getFavoriteEvents(author)
            Log.d("MyTag", "loadFavoriteEvents: $events")
            _state.value = ScreenState.Success(events)

        }
        _isLoading.value = false
    }


}