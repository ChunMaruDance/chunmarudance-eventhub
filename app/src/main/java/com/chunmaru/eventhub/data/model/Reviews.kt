package com.chunmaru.eventhub.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

data class Review(
    val eventId: String,
    val authorEventId: String,
    val authorReviewId: String,
    val reviewId: String,
    val text: String,
    val date: String
) {
    fun getFormattedDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        val reviewDate = dateFormat.parse(date) ?: throw IllegalStateException("Error")

        val diffInMillis = abs(currentDate.time - reviewDate.time)
        val diffInSeconds = diffInMillis / 1000
        val diffInMinutes = diffInSeconds / 60
        val diffInHours = diffInMinutes / 60
        val diffInDays = diffInHours / 24

        return when {
            diffInDays > 7 -> dateFormat.format(reviewDate) // If more than a week, show the full date
            diffInDays >= 1 -> "${diffInDays.toInt()} days ago"
            diffInHours >= 1 -> "${diffInHours.toInt()} hours ago"
            diffInMinutes >= 1 -> "${diffInMinutes.toInt()} minutes ago"
            else -> "Just now"
        }
    }
}

data class FirebaseReview(
    val eventId: String?,
    val authorEventId: String?,
    val authorReviewId:String?,
    val reviewId: String?,
    val reviewText: String?,
    val date: String?
) {

    constructor() : this(null, null, null, null, null,null)

    fun toReview(): Review = Review(
        eventId!!,
        authorEventId!!,
        authorReviewId!!,
        reviewId!!,
        reviewText!!,
        date!!
    )
}


data class ReviewAuthor(
    val auhtor: Author,
    val review: String,
    val reviewId: String,
    val eventId: String,
    val date: String
)
