package com.chunmaru.eventhub.screens.change_profile_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.ImageData
import com.chunmaru.eventhub.data.model.ImageResult
import com.chunmaru.eventhub.domain.AuthorRepository
import com.chunmaru.eventhub.domain.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notify
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class ChangeProfileScreenViewModel @Inject constructor(
    private val authorRepository: AuthorRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private var _profileData: MutableStateFlow<ChangeProfileScreenState> =
        MutableStateFlow(ChangeProfileScreenState.Initial)
    val profileData: StateFlow<ChangeProfileScreenState> = _profileData

    private lateinit var startedAvatar: String

    fun setProfile(author: Author) {
        _profileData.value = ChangeProfileScreenState.ShowProfile(author)
        startedAvatar = author.avatar.uri
    }

    fun setAvatar(avatarUri: String) {
        val currentState = _profileData.value
        if (currentState is ChangeProfileScreenState.ShowProfile) {
            _profileData.value = currentState.copy(
                author = currentState.author.copy(
                    avatar = ImageData(
                        uri = avatarUri,
                        path = currentState.author.avatar.path
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
        if (currentState is ChangeProfileScreenState.ShowProfile) {
            _profileData.value = ChangeProfileScreenState.Pending
            viewModelScope.launch(Dispatchers.IO) {

                updateNameAndDescription(userName, userDescription, currentState)

                if (startedAvatar != currentState.author.avatar.uri) {
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
        currentState: ChangeProfileScreenState.ShowProfile
    ) {
        if (userName != currentState.author.name) {
            authorRepository.setName(userName)
        }

        if (userDescription != currentState.author.description) {
            authorRepository.setDescription(userDescription)
        }
    }

    private suspend fun handleAvatarChange(
        currentState: ChangeProfileScreenState.ShowProfile,
        onError: (Exception) -> Unit
    ) {
        val data = storageRepository.setAvatar(
            currentAuthor = currentState.author,
            uri = currentState.author.avatar.uri
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