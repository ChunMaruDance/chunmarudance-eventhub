package com.chunmaru.eventhub.domain.repositories

import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.ImageResult

interface StorageRepository {

    suspend fun setAvatar(currentAuthor: Author, uri: String): ImageResult

    suspend fun saveEventImage(currentAuthorId: String, event: Event): ImageResult

}