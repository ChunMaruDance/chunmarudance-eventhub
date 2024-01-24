package com.chunmaru.eventhub.data.model.optional


data class MenuItem(
    val title: String,
    val isSelect: Boolean
) {

    companion object {

        val DEFAULT_ITEMS_TO_SEARCH = listOf(
            MenuItem(
                title = "Event name",
                isSelect = true
            ),
            MenuItem(
                title = "Author name",
                isSelect = true
            ),
            MenuItem(
                title = "City name",
                isSelect = true
            )
        )

    }

}