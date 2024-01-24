package com.chunmaru.eventhub.screens.change_profile_screen


import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.screens.change_profile_screen.change_profile_elements.ChangeProfileScreenContent
import com.chunmaru.eventhub.screens.change_profile_screen.change_profile_elements.ChangeProfileTopBar

@Composable
fun ChangeProfileScreen(
    author: Author,
    onBackClick: () -> Unit,
    onSaveData: () -> Unit
) {

    val viewModel: ChangeProfileScreenViewModel = hiltViewModel()
    val state = viewModel.profileData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setProfile(author)
    }


    when (val localeState = state.value) {

        ChangeProfileScreenState.Initial -> {

        }

        ChangeProfileScreenState.Pending -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 20.dp),
                    color = Color(android.graphics.Color.parseColor("#bdec3a"))
                )
            }


        }

        is ChangeProfileScreenState.ShowProfile -> {

            val reg =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.data?.let { uri ->
                            viewModel.setAvatar(uri.toString())
                        }
                    }
                }
            val description = remember { mutableStateOf(localeState.author.description) }
            val userName = remember { mutableStateOf(localeState.author.name) }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = Color.Transparent,
                topBar = {
                    ChangeProfileTopBar(
                        onBackClick = {
                            onBackClick()
                        },
                        onSaveClick = {
                            viewModel.saveChanged(
                                userName = userName.value,
                                userDescription = description.value,
                                onSuccess = {
                                    onSaveData()
                                }
                            )
                        }
                    )
                }
            ) { paddingValues ->

                ChangeProfileScreenContent(
                    localeState.author,
                    description = description.value,
                    userName = userName.value,
                    onAvatarChangeClick = {
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        reg.launch(intent)
                    },
                    onDescriptionChange = {
                        description.value = it
                    },
                    onUserNameChange = {
                        userName.value = it
                    }
                )

            }


        }
    }


}