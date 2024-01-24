package com.chunmaru.eventhub.screens.favorite_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.domain.AuthorRepository
import com.chunmaru.eventhub.domain.EventRepository
import com.chunmaru.eventhub.screens.favorite_screen.FavoriteScreenState.Initial
import com.chunmaru.eventhub.screens.favorite_screen.FavoriteScreenState.PendingResult
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


    private var _state: MutableStateFlow<FavoriteScreenState> =
        MutableStateFlow(Initial)
    val state = _state.asStateFlow()

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadFavorite() {
        _state.value = PendingResult
        loadFavoriteEvents()
    }

    fun removeFromFavorite(event: Event) {
        val currentState = _state.value as? FavoriteScreenState.ShowData ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val events = currentState.eventList.toMutableList()
            events.remove(event)
            _state.value = currentState.copy(eventList = events)
            authorRepository.removeLikeEvent(event.id)
        }
    }

    fun loadFavoriteEvents() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = PendingResult
            val author = authorRepository.getAuthor(Firebase.auth.uid!!)
            val events = eventRepository.getFavoriteEvents(author)
            Log.d("MyTag", "loadFavoriteEvents: $events")
            _state.value = FavoriteScreenState.ShowData(events)

        }
        _isLoading.value = false
    }


}