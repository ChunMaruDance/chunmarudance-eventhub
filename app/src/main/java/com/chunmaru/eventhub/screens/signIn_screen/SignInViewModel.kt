package com.chunmaru.eventhub.screens.signIn_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chunmaru.eventhub.data.model.FireBaseAuthor
import com.chunmaru.eventhub.domain.AuthorRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authorRepository: AuthorRepository,
    val checkSignInFireBase: CheckSignInFireBase
) : ViewModel() {

    fun createAuthor() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authorRepository.getAuthor(Firebase.auth.uid!!)
            } catch (e: Exception) {
                if (authorRepository.createAuthor(
                        FireBaseAuthor(
                            id = Firebase.auth.uid!!,
                            name = Firebase.auth.currentUser?.displayName ?: "None",
                            description = "",
                            avatar = null,
                            events = null,
                            likedEvents = null,
                            deletedIds = null
                        )
                    )
                ) Log.d("MyTag", "createAuthor: success")
                else Log.d("MyTag", "createAuthor: error")


            }


        }
    }

}