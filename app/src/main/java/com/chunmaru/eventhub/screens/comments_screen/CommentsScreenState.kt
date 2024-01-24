package com.chunmaru.eventhub.screens.comments_screen

import com.chunmaru.eventhub.data.model.ReviewAuthor


sealed class CommentsScreenState {

    object Initial : CommentsScreenState()

    object PendingResult : CommentsScreenState()

    data class ShowData(
        val reviews: List<ReviewAuthor>,
       val  isAuthor: Boolean
    ) : CommentsScreenState()

}