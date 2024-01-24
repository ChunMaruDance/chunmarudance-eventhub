package com.chunmaru.eventhub.data.model.event

import com.chunmaru.eventhub.data.model.FirebaseImageData
import com.chunmaru.eventhub.data.model.ImageData


data class Event(
    val id: String,
    val name: String,
    val description: String,
    val date: String,
    val time: String,
    val imgUri: ImageData,
    val categories: List<String>,
    val city: String,
    val authorId: String
)

data class FireBaseEvent(
    val id: String?,
    val name: String?,
    val description: String?,
    val date: String?,
    val time: String?,
    val imgUri: FirebaseImageData?,
    val categories: List<String>?,
    val city: String?,
    val fullAddress: String?,
    val authorId: String?
) {
    constructor() : this(
        null, null,
        null, null,
        null, null,
        null, null,
        null, null
    )

    fun toEvent(): Event {

        return Event(
            id = id!!,
            name = name!!,
            description = description!!,
            date = date!!,
            time = time!!,
            imgUri = imgUri!!.toImageData(),
            categories = categories!!,
            authorId = authorId!!,
            city = city!!,
        )

    }


}