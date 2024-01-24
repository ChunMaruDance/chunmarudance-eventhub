package com.chunmaru.eventhub.domain.repositories

import com.chunmaru.eventhub.data.model.event.EventReviewCount
import com.chunmaru.eventhub.data.model.reviews.Review

interface ReviewsRepository {

    suspend fun addReview(eventId: String, review: Review)

    suspend fun deleteReview(eventId: String, reviewId: String)

    suspend fun getAllReview(eventId: String): List<Review>

    suspend fun deleteAllReviews(eventId: String): Boolean

    suspend fun getEventCountsInReviews(): List<EventReviewCount>

}