package com.chunmaru.eventhub.data.repositories

import android.util.Log
import com.chunmaru.eventhub.data.FirebaseKnot
import com.chunmaru.eventhub.data.model.event.EventReviewCount
import com.chunmaru.eventhub.data.model.reviews.FirebaseReview
import com.chunmaru.eventhub.data.model.reviews.Review
import com.chunmaru.eventhub.domain.repositories.ReviewsRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ReviewsRepositoryImpl : ReviewsRepository {

    private val database = Firebase.database.reference.child(FirebaseKnot.REVIEWS)
    override suspend fun addReview(eventId: String, review: Review) {
        try {
            val eventReviewsRef = database.child(eventId)
            val reviewKey =
                eventReviewsRef.push().key
                    ?: throw Exception("Failed to get a key for the new review")
            Log.d("MyTag", "addReview: $review ")
            val reviewData = mapOf(
                "eventId" to review.eventId,
                "authorEventId" to review.authorEventId,
                "authorReviewId" to review.authorReviewId,
                "reviewId" to reviewKey,
                "reviewText" to review.text,
                "date" to review.date
            )

            eventReviewsRef.child(reviewKey).setValue(reviewData).await()
        } catch (e: Exception) {
            Log.d("MyTag", "Error adding review: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteReview(eventId: String, reviewId: String) {
        val eventReviewsRef = database.child(eventId)
        eventReviewsRef.child(reviewId).removeValue().await()
    }

    override suspend fun getAllReview(eventId: String): List<Review> {
        val eventReviewsRef = database.child(eventId)

        return suspendCoroutine { continuation ->
            eventReviewsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val reviews = mutableListOf<Review>()

                    for (childSnapshot in snapshot.children) {
                        val review = childSnapshot.getValue(FirebaseReview::class.java)
                        Log.d("MyTag", "onDataChange: $review ")
                        review?.let { reviews.add(it.toReview()) }
                    }
                    continuation.resume(reviews)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }

    override suspend fun deleteAllReviews(eventId: String): Boolean {
        return try {
            val reviews = getAllReview(eventId)

            for (review in reviews) {
                deleteReview(eventId, review.reviewId)
            }
            true
        } catch (e: Exception) {
            Log.d("MyTag", "Error deleting all reviews: $e")
            false
        }
    }

    override suspend fun getEventCountsInReviews(): List<EventReviewCount> {
        val eventRepository = EventRepositoryImpl()
        val eventReviewCounts = mutableListOf<EventReviewCount>()

        try {
            val snapshot = database.get().await()
            for (eventSnapshot in snapshot.children) {
                val eventId = eventSnapshot.key
                eventId?.let {
                    val reviewsCount = eventSnapshot.childrenCount.toInt()
                    val eventName = eventRepository.getEventName(eventId)
                    Log.d("MyTag", "getEventCountsInReviews: $eventName : $reviewsCount ")
                    eventReviewCounts.add(EventReviewCount(eventName, reviewsCount))
                }
            }

        } catch (e: Exception) {
            Log.d("MyTag", "Error getting event counts in reviews: $e")
            throw e
        }
        Log.d("MyTag", "getEventCountsInReviews: $eventReviewCounts ")
        return eventReviewCounts
    }


}