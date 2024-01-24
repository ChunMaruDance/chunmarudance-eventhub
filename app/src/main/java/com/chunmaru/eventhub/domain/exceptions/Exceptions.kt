package com.chunmaru.eventhub.domain.exceptions


class ImageSizeExceededException(message: String) : Exception(message) {
    companion object {
        val DEFAULT = ImageSizeExceededException("Image size greater than 10 MB")
    }
}