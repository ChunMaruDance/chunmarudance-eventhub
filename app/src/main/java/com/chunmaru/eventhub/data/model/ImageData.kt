package com.chunmaru.eventhub.data.model

import com.chunmaru.eventhub.R

sealed class ImageResult {
    data class Success(val imageData: ImageData) : ImageResult()
    data class Failure(val exception: Exception) : ImageResult()
}


data class ImageData(
    val uri: String,
    val path: String
) {
    fun toFirebaseImageData(): FirebaseImageData = FirebaseImageData(
        uri = uri,
        path = path
    )
}

data class FirebaseImageData(
    val uri: String?,
    val path: String?
) {
    constructor() : this(
        null, null
    )

    fun toImageData(): ImageData = ImageData(
        uri = uri
            ?: "android.resource://${"com.chunmaru.eventhub"}/${R.drawable.default_user}",
        path = path ?: ""
    )

}