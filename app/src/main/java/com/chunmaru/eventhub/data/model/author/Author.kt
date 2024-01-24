package com.chunmaru.eventhub.data.model.author

import com.chunmaru.eventhub.R
import com.chunmaru.eventhub.data.model.FirebaseImageData
import com.chunmaru.eventhub.data.model.ImageData

data class Author(
    val id: String,
    val name: String,
    val description: String,
    val avatar: ImageData,
    val events: List<String>,
    val likedEvents: List<String>,
    val deletedIds: List<String>
)

data class FireBaseAuthor(
    val id: String?,
    val name: String?,
    val description: String?,
    val avatar: FirebaseImageData?,
    val events: Map<String, String>?,
    val likedEvents: Map<String, String>?,
    val deletedIds: Map<String, String>?
) {
    constructor() : this(
        null, null, null,
        null, null,
        null, null,
    )

    fun toAuthor(): Author =
        Author(
            id = id!!,
            name = name!!,
            description = description ?: " ",
            avatar = ImageData(
                uri = avatar?.uri?.takeIf { it.length >= 2 } ?: defaultUri,
                path = avatar?.path?.takeIf { it.length >= 2 } ?: defaultPath
            ),
            events = events?.values?.toList() ?: listOf(),
            likedEvents = likedEvents?.values?.toList() ?: listOf(),
            deletedIds = deletedIds?.values?.toList() ?: listOf()
        )


    companion object {
        val defaultUri =
            "android.resource://${"com.chunmaru.eventhub"}/${R.drawable.default_user}"
        const val defaultPath = "default"
    }


}



