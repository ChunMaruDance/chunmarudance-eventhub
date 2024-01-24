package com.chunmaru.eventhub.domain.repositories

import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.AuthorEventCount
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.author.FireBaseAuthor
import com.chunmaru.eventhub.data.model.ImageData

interface AuthorRepository {

    fun setName(newName: String): Boolean

    fun setDescription(newDescriptor: String): Boolean

    fun setAvatar(newAvatar: ImageData): Boolean

    fun signOut(): Boolean

    suspend fun getAuthorsEventCounts(): List<AuthorEventCount>

    suspend fun getAuthor(uid: String): Author

    suspend fun deleteAuthor(uid: String): Boolean

    suspend fun createAuthor(author: FireBaseAuthor): Boolean

    suspend fun likeEvent(eventId: String): Boolean

    suspend fun removeLikeEvent(eventId: String): Boolean

    suspend fun selectEvent(eventId: String): Boolean

    suspend fun addEvent(eventId: String): Boolean

    suspend fun deleteEvent(event: Event): Boolean

    suspend fun getAuthorByEventId(eventId: String): Author?

    suspend fun searchAuthor(query: String): List<Author>

    suspend fun hasLikedEvent(eventId: String): Boolean

    suspend fun addToDeletedId(eventId: String, authorId: String): Boolean

    suspend fun deleteDeletedId(eventId: String, authorId: String): Boolean

    suspend fun deleteDeletedIds(): Boolean

}