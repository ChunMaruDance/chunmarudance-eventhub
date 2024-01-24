package com.chunmaru.eventhub.domain

import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.data.model.ImageData
import com.chunmaru.eventhub.data.model.ImageResult

interface StorageRepository {

    suspend fun setAvatar(currentAuthor: Author, uri: String): ImageResult

    suspend fun saveEventImage(currentAuthorId: String, event: Event): ImageResult

}