package com.chunmaru.eventhub.screens.comments_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.data.model.Review
import com.chunmaru.eventhub.data.model.ReviewAuthor
import com.chunmaru.eventhub.domain.AuthorRepository
import com.chunmaru.eventhub.domain.ReviewsRepository
import com.chunmaru.eventhub.screens.show_event_screen.ShowEventScreenState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CommentsScreenViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepository,
    private val authorRepository: AuthorRepository
) : ViewModel() {

    private var _state: MutableStateFlow<CommentsScreenState> =
        MutableStateFlow(CommentsScreenState.Initial)
    val state = _state.asStateFlow()

    private lateinit var event: Event

    private lateinit var currentAuthor: Author

    init {
        viewModelScope.launch(Dispatchers.IO) {
            currentAuthor = authorRepository.getAuthor(Firebase.auth.uid!!)
        }
    }


    fun setEvent(event: Event) {
        this.event = event
        loadComments()
    }

    private fun loadComments() {
        viewModelScope.launch(Dispatchers.IO) {
            val reviews = reviewsRepository.getAllReview(event.id)
            val reviewAuthors = mutableListOf<ReviewAuthor>()

            for (review in reviews) {
                val author = authorRepository.getAuthor(review.authorReviewId)
                val reviewAuthor = ReviewAuthor(
                    auhtor = author,
                    review = review.text,
                    date = review.getFormattedDate(),
                    reviewId = review.reviewId,
                    eventId = review.eventId
                )
                reviewAuthors.add(reviewAuthor)
            }
            Log.d("MyTag", "showComments: $reviews ")
            Log.d("MyTag", "showComments author: $reviewAuthors ")
            _state.value =
                CommentsScreenState.ShowData(
                    isAuthor = event.authorId == Firebase.auth.uid,
                    reviews = reviewAuthors
                )


        }
    }

    fun deleteComment(reviewAuthor: ReviewAuthor) {
        val currentState = _state.value as? CommentsScreenState.ShowData ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val reviews = currentState.reviews.toMutableList()
            reviews.remove(reviewAuthor)
            val stateLocale = CommentsScreenState.ShowData(
                reviews = reviews,
                isAuthor = currentState.isAuthor
            )
            _state.value = stateLocale
            reviewsRepository.deleteReview(eventId = event.id, reviewId = reviewAuthor.reviewId)
        }

    }

    fun addReview(text: String) {
        if (text.length > 20) {
            val currentEvent = event
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            val review = Review(
                eventId = currentEvent.id,
                authorReviewId = Firebase.auth.uid!!,
                authorEventId = event.authorId,
                reviewId = "",
                text = text,
                date = currentDate
            )

            val currentState = _state.value as? CommentsScreenState.ShowData ?: return
            val updatedReviews = currentState.reviews.toMutableList()
            val newReviewAuthor = ReviewAuthor(
                auhtor = currentAuthor,
                review = review.text,
                date = review.getFormattedDate(),
                reviewId = review.reviewId,
                eventId = review.eventId
            )
            updatedReviews.add(newReviewAuthor)

            _state.value = CommentsScreenState.ShowData(
                isAuthor = currentState.isAuthor,
                reviews = updatedReviews
            )

            viewModelScope.launch(Dispatchers.IO) {
                reviewsRepository.addReview(currentEvent.id, review)
            }

        }

    }

}