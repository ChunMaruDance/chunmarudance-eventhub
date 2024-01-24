package com.chunmaru.eventhub.screens.change_profile_screen

import com.chunmaru.eventhub.data.model.Author

sealed class ChangeProfileScreenState {

    object Initial : ChangeProfileScreenState()

    object Pending : ChangeProfileScreenState()

    data class ShowProfile(
        val author: Author
    ) : ChangeProfileScreenState()


}