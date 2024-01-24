package com.chunmaru.eventhub.presentation.screens.change_profile_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.ImageData
import com.chunmaru.eventhub.data.model.ImageResult
import com.chunmaru.eventhub.domain.repositories.AuthorRepository
import com.chunmaru.eventhub.domain.repositories.StorageRepository
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChangeProfileScreenViewModel @Inject constructor(
    private val authorRepository: AuthorRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private var _profileData: MutableStateFlow<ScreenState<Author>> =
        MutableStateFlow(ScreenState.Initial())
    val profileData: StateFlow<ScreenState<Author>> = _profileData

    private lateinit var startedAvatar: String

    fun setProfile(author: Author) {
        _profileData.value = ScreenState.Success(author)
        startedAvatar = author.avatar.uri
    }

    fun setAvatar(avatarUri: String) {
        val currentState = _profileData.value
        if (currentState is ScreenState.Success) {
            _profileData.value = currentState.copy(
                data = currentState.data.copy(
                    avatar = ImageData(
                        uri = avatarUri,
                        path = currentState.data.avatar.path
                    )
                )
            )
        }
    }

    fun saveChanged(
        userName: String,
        userDescription: String,
        onSuccess: () -> Unit,
    ) {
        val currentState = _profileData.value
        if (currentState is ScreenState.Success) {
            _profileData.value = ScreenState.Pending()
            viewModelScope.launch(Dispatchers.IO) {

                updateNameAndDescription(userName, userDescription, currentState)

                if (startedAvatar != currentState.data.avatar.uri) {
                    handleAvatarChange(currentState, onError = {

                    })
                }

                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    private fun updateNameAndDescription(
        userName: String,
        userDescription: String,
        currentState: ScreenState.Success<Author>
    ) {
        if (userName != currentState.data.name) {
            authorRepository.setName(userName)
        }

        if (userDescription != currentState.data.description) {
            authorRepository.setDescription(userDescription)
        }
    }

    private suspend fun handleAvatarChange(
        currentState: ScreenState.Success<Author>,
        onError: (Exception) -> Unit
    ) {
        val data = storageRepository.setAvatar(
            currentAuthor = currentState.data,
            uri = currentState.data.avatar.uri
        )

        when (data) {
            is ImageResult.Failure -> {
                onError(data.exception)
            }

            is ImageResult.Success -> {
                authorRepository.setAvatar(data.imageData)
            }
        }
    }


}