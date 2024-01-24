package com.chunmaru.eventhub.presentation.screens.comments_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.reviews.Review
import com.chunmaru.eventhub.data.model.reviews.ReviewAuthor
import com.chunmaru.eventhub.data.model.reviews.ReviewsIsAuthor
import com.chunmaru.eventhub.domain.repositories.AuthorRepository
import com.chunmaru.eventhub.domain.repositories.ReviewsRepository
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
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

    private var _state: MutableStateFlow<ScreenState<ReviewsIsAuthor>> =
        MutableStateFlow(ScreenState.Initial())
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
                ScreenState.Success(
                    ReviewsIsAuthor(
                        isAuthor = event.authorId == Firebase.auth.uid,
                        reviews = reviewAuthors
                    )
                )


        }
    }

    fun deleteComment(reviewAuthor: ReviewAuthor) {
        val currentState = _state.value as? ScreenState.Success ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val reviews = currentState.data.reviews.toMutableList()
            reviews.remove(reviewAuthor)
            val stateLocale = ScreenState.Success(
                ReviewsIsAuthor(
                    reviews = reviews,
                    isAuthor = currentState.data.isAuthor
                )
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

            val currentState = _state.value as? ScreenState.Success ?: return
            val updatedReviews = currentState.data.reviews.toMutableList()
            val newReviewAuthor = ReviewAuthor(
                auhtor = currentAuthor,
                review = review.text,
                date = review.getFormattedDate(),
                reviewId = review.reviewId,
                eventId = review.eventId
            )
            updatedReviews.add(newReviewAuthor)

            _state.value = ScreenState.Success(
                ReviewsIsAuthor(
                    isAuthor = currentState.data.isAuthor,
                    reviews = updatedReviews
                )

            )

            viewModelScope.launch(Dispatchers.IO) {
                reviewsRepository.addReview(currentEvent.id, review)
            }

        }

    }

}