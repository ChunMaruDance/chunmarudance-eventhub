package com.chunmaru.eventhub.data.model.event

data class DeletedEventInfo(
    val eventId: String,
    val eventName: String,
    val authorId: String,
    val reason: String,
)

data class FireBaseDeletedEventInfo(
    val eventId: String?,
    val eventName: String?,
    val authorId: String?,
    val reason: String?
) {

    constructor() : this(
        null, null, null, null,
    )

    fun toDeletedEventInfo(): DeletedEventInfo = DeletedEventInfo(
        eventId = eventId!!,
        eventName = eventName!!,
        authorId = authorId!!,
        reason = reason!!
    )

}