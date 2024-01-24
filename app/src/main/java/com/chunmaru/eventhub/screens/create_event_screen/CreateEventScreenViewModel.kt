package com.chunmaru.eventhub.screens.create_event_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.data.model.EventType
import com.chunmaru.eventhub.data.model.ImageData
import com.chunmaru.eventhub.data.model.ImageResult
import com.chunmaru.eventhub.data.model.ReviewAuthor
import com.chunmaru.eventhub.domain.AuthorRepository
import com.chunmaru.eventhub.domain.EventRepository
import com.chunmaru.eventhub.domain.EventTypeRepository
import com.chunmaru.eventhub.domain.ReviewsRepository
import com.chunmaru.eventhub.domain.StorageRepository
import com.chunmaru.eventhub.screens.create_event_screen.CreateEventScreenState.Initial
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateEventScreenViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authorRepository: AuthorRepository,
    private val eventTypeRepository: EventTypeRepository,
    private val storageRepository: StorageRepository,
    private val reviewsRepository: ReviewsRepository
) : ViewModel() {

    private var _state: MutableStateFlow<CreateEventScreenState> = MutableStateFlow(Initial)
    val state = _state.asStateFlow()

    private var initialEvent: Event? = null
    private var isNewEvent: Boolean = false

    private fun updateEvent(update: (Event) -> Event) {
        _state.value = (state.value as? CreateEventScreenState.CreateEvent)?.let {
            it.copy(event = update(it.event))
        } ?: state.value
    }

    fun setAvatar(uri: String) {
        updateEvent { it.copy(imgUri = ImageData(uri, it.imgUri.path)) }
    }

    fun setEventName(name: String) {
        updateEvent { it.copy(name = name) }
    }

    fun setEventDescription(description: String) {
        updateEvent { it.copy(description = description) }
    }

    fun setDate(date: String?) {
        updateEvent { it.copy(date = date.orEmpty()) }
    }

    fun setTime(time: String?) {
        updateEvent { it.copy(time = time.orEmpty()) }
    }

//    fun showComments() {
//        val localeState = _state.value as? CreateEventScreenState.CreateEvent ?: return
//        if (localeState.comments is ShowCommentsState.ShowComments) return
//        viewModelScope.launch(Dispatchers.IO) {
//            val reviews = reviewsRepository.getAllReview(localeState.event.id)
//
//            val reviewAuthors = mutableListOf<ReviewAuthor>()
//
//            for (review in reviews) {
//                val author = authorRepository.getAuthor(review.authorId)
//                val reviewAuthor = ReviewAuthor(author, review.text)
//                reviewAuthors.add(reviewAuthor)
//            }
//            Log.d("MyTag", "showComments: $reviews ")
//            Log.d("MyTag", "showComments author: $reviewAuthors ")
//            _state.value = CreateEventScreenState.CreateEvent(
//                event = localeState.event,
//                allTypes = localeState.allTypes,
//                comments = ShowCommentsState.ShowComments(reviewAuthors)
//            )
//
//        }
//
//
//    }


    fun setCategories(category: String) {
        _state.value =
            (state.value as? CreateEventScreenState.CreateEvent)?.let { eventScreenState ->
                val updatedCategories = eventScreenState.event.categories.toMutableList()

                if (updatedCategories.contains(category)) {
                    updatedCategories.remove(category)
                } else {
                    updatedCategories.add(category)
                }

                val updatedEvent = eventScreenState.event.copy(categories = updatedCategories)

                val updatedAllTypes = eventScreenState.allTypes.map {
                    if (it.type == category) {
                        it.copy(selected = !it.selected)
                    } else {
                        it
                    }
                }
                CreateEventScreenState.CreateEvent(
                    updatedEvent,
                    updatedAllTypes
                )
            } ?: state.value
    }

    fun saveEvent(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val currentState = _state.value as? CreateEventScreenState.CreateEvent ?: return
        if (currentState.event.name.length < 5 || currentState.event.categories.isEmpty()) return

        _state.value = CreateEventScreenState.Pending
        viewModelScope.launch {
            var event: Event = currentState.event
            try {
                if (initialEvent?.imgUri?.uri != event.imgUri.uri) {
                    val saveState = storageRepository.saveEventImage(
                        Firebase.auth.uid!!,
                        event
                    )
                    if (saveState is ImageResult.Success) {
                        Log.d("MyTag", "saveEvent 3131313: ${saveState.imageData} ")
                        event = event.copy(imgUri = saveState.imageData)
                    } else {
                        Log.d("MyTag", "saveEvent 3131313: ${saveState.toString()} ")
                    }
                }
                if (isNewEvent) {
                    val eventId = eventRepository.createEvent(event = event)
                    authorRepository.addEvent(eventId)
                    onSuccess()
                } else {
                    Log.d("MyTag", "saveEvent: $event ")
                    eventRepository.updateEvent(event)
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }

        }

    }

    fun setUp(event: Event) {
        isNewEvent = event.id.length < 5
        initialEvent = event.copy()

        _state.value = CreateEventScreenState.Pending
        viewModelScope.launch(Dispatchers.IO) {
            eventTypeRepository.getAllType().collect { allDataBaseTypes ->
                _state.value = CreateEventScreenState.CreateEvent(
                    event,
                    allTypes = allDataBaseTypes.map {
                        EventType(
                            type = it,
                            selected = event.categories.contains(it)
                        )
                    },
                )

            }

        }
    }

    fun deleteEvent(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val currentState = _state.value as? CreateEventScreenState.CreateEvent ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val reviewDeferred =
                    async { reviewsRepository.deleteAllReviews(currentState.event.id) }
                val authorDeferred = async { authorRepository.deleteEvent(currentState.event) }
                val eventDeferred = async { eventRepository.deleteEvent(currentState.event) }

                val authorResult = authorDeferred.await()
                val eventResult = eventDeferred.await()
                val reviewResult = reviewDeferred.await()

                if (authorResult && eventResult && reviewResult) {
                    Log.d("MyTag", "Обидва завдання виконано успішно")
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else {
                    Log.d("MyTag", "Помилки виконання завдань")
                    withContext(Dispatchers.Main) {
                        onError()
                    }

                }

            } catch (e: Exception) {
                Log.d("MyTag", "deleteEvent: $e ")
                onError()
            }
        }
    }

    fun setCity(newCity: String) {
        updateEvent { it.copy(city = newCity) }
    }

}