package com.chunmaru.eventhub.screens.show_event_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.R
import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.data.model.ImageData
import com.chunmaru.eventhub.data.model.Review
import com.chunmaru.eventhub.domain.AuthorRepository
import com.chunmaru.eventhub.domain.ReviewsRepository
import com.chunmaru.eventhub.screens.show_event_screen.ShowEventScreenState.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ShowEventScreenViewModel @Inject constructor(
    private val authorRepository: AuthorRepository,
    private val reviewsRepository: ReviewsRepository
) : ViewModel() {

    private var _state: MutableStateFlow<ShowEventScreenState> =
        MutableStateFlow(Initial)
    val state = _state.asStateFlow()

    fun setEvent(event: Event) {
        setEventData(event)
    }

    private fun hasEventOccurred(event: Event): Boolean {
        val eventDateTime = LocalDateTime.parse(
            "${event.date} ${event.time}",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )
        val currentDateTime = LocalDateTime.now()

        return currentDateTime.isAfter(eventDateTime)
    }

    fun addReview(text: String) {
        if (text.length > 20) {
            val currentEvent = (_state.value as? ShowEvent)?.event
                ?: return


            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            val review = Review(
                eventId = currentEvent.id,
                authorReviewId = Firebase.auth.uid!!,
                authorEventId = currentEvent.authorId,
                reviewId = "",
                text = text,
                date = currentDate
            )
            viewModelScope.launch(Dispatchers.IO) {
                reviewsRepository.addReview(currentEvent.id, review)
            }

        }

    }


    fun likeEvent(eventId: String) {
        val currentState = state.value as? ShowEvent ?: return
        Log.d("MyTag", "addToFavorite: ")

        viewModelScope.launch(Dispatchers.IO) {
            val isAlreadyLiked = currentState.author.likedEvents.contains(eventId)
            Log.d("MyTag", "isAlreadyLiked: $isAlreadyLiked ")
            if (isAlreadyLiked) {
                authorRepository.removeLikeEvent(eventId)
            } else {
                authorRepository.likeEvent(eventId)
            }

            val updatedAuthor = currentState.author.copy(
                likedEvents = if (isAlreadyLiked) {
                    currentState.author.likedEvents - eventId
                } else {
                    currentState.author.likedEvents + eventId
                }
            )

            _state.value = currentState.copy(author = updatedAuthor, favorite = !isAlreadyLiked)
        }
    }

    private fun setEventData(event: Event) {
        _state.value = PendingDate
        viewModelScope.launch(Dispatchers.IO) {
            val author = authorRepository.getAuthor(event.authorId)
            _state.value = ShowEvent(
                event = event,
                author = author,
                favorite = authorRepository.hasLikedEvent(event.id),
                isHeld = hasEventOccurred(event)
            )
        }
    }

}