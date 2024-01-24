package com.chunmaru.eventhub.presentation.screens.profile_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.author.AuthorEvents
import com.chunmaru.eventhub.data.model.event.DeletedEventInfo
import com.chunmaru.eventhub.domain.repositories.AdminsRepository
import com.chunmaru.eventhub.domain.repositories.AuthorRepository
import com.chunmaru.eventhub.domain.repositories.DeletedEventsMessageRepository
import com.chunmaru.eventhub.domain.repositories.EventRepository
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authorRepository: AuthorRepository,
    private val eventRepository: EventRepository,
    private val adminsRepository: AdminsRepository,
    private val deletedEventsMessageRepository: DeletedEventsMessageRepository
) : ViewModel() {

    private var _profile: MutableStateFlow<ScreenState<AuthorEvents>> =
        MutableStateFlow(ScreenState.Initial())
    val profile: StateFlow<ScreenState<AuthorEvents>> = _profile

    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var _messages = MutableStateFlow<List<DeletedEventInfo>>(listOf())
    val messages = _messages.asStateFlow()

    init {
        _profile.value = ScreenState.Pending()
        loadProfile()
    }

    private fun loadMessages(author: Author) {
        viewModelScope.launch(Dispatchers.IO) {
            val messages =
                deletedEventsMessageRepository.getDeletedEventsByAuthor(author.deletedIds)
            _messages.value = messages

        }
    }

    fun loadProfile() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val author = authorRepository.getAuthor(Firebase.auth.uid!!)
                val eventsFlow = eventRepository.getEventByAuthorId(Firebase.auth.uid!!)

                eventsFlow.collect { events ->
                    withContext(Dispatchers.Main) {
                        val currentState =
                            ScreenState.Success(
                                AuthorEvents(
                                    author,
                                    events,
                                    adminsRepository.isCurrentUserAdmin()
                                )
                            )
                        _profile.value = currentState
                        _isLoading.value = false
                        loadMessages(author)
                    }
                }

            } catch (e: Exception) {
                Log.e("MyTag", "Error loading profile", e)
            }
        }
    }

    fun signOut() {
        authorRepository.signOut()
    }

    fun clearMessage() {
        viewModelScope.launch {
            if (deletedEventsMessageRepository.deleteAllEventMessagesForAuthor(_messages.value.map {
                    it.eventId
                }) && authorRepository.deleteDeletedIds()) {
                Log.d("MyTag", "deletedSuccess")
            }
            _messages.value = emptyList()
        }


    }

}