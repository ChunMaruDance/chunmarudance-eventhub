package com.chunmaru.eventhub.presentation.screens.profile_screen


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.presentation.default_elements.DefaultProgressBar
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element.ModalSheetContent
import com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element.ProfileCard
import com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element.ProfileRowEventsCard
import com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element.SignOutAlertDialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onChangeProfileClick: (Author) -> Unit,
    onCreateEventClick: () -> Unit,
    onShowFavoriteClick: () -> Unit,
    onSignInClick: () -> Unit,
    onEventClick: (Event) -> Unit,
    onAdminClick: () -> Unit
) {

    val viewModel: ProfileScreenViewModel = hiltViewModel()
    val state = viewModel.profile.collectAsState()

    val messages = viewModel.messages.collectAsState()

    when (val localeState = state.value) {

        is ScreenState.Initial -> {

        }

        is ScreenState.Pending -> {
            DefaultProgressBar()
        }

        is ScreenState.Success -> {


            if (messages.value.isNotEmpty()) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.clearMessage()
                    },
                    title = {
                        Text(
                            text = "Some events have been deleted!!!",
                            color = Color(
                                android.graphics.Color.parseColor(
                                    "#bdec3a"
                                )
                            )
                        )
                    },
                    text = {
                        Text(
                            text = messages.value.joinToString("\n") {
                                "Event: ${it.eventName} deleted\nReason: ${it.reason}"
                            },
                            color = Color.Gray
                        )
                    },
                    buttons = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = {
                                    viewModel.clearMessage()
                                }
                            ) {
                                Text(
                                    text = "Ok",
                                    color = Color(
                                        android.graphics.Color.parseColor(
                                            "#bdec3a"
                                        )
                                    )
                                )
                            }

                        }
                    },
                    backgroundColor = Color(android.graphics.Color.parseColor("#1A1A1A")),
                    modifier = Modifier
                        .width(350.dp)
                        .background(
                            color = Color(android.graphics.Color.parseColor("#1A1A1A")),
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }


            val openBottomSheet = rememberSaveable {
                mutableStateOf(false)
            }

            val isLoading = viewModel.isLoading.collectAsState()
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading.value)
            val showAlertDialog = remember {
                mutableStateOf(false)
            }
            SwipeRefresh(state = swipeRefreshState,
                onRefresh = {
                    viewModel.loadProfile()
                },
                indicator = { indicatorState, refreshTrigger ->
                    SwipeRefreshIndicator(
                        state = indicatorState, refreshTriggerDistance = refreshTrigger,
                        backgroundColor = Color.Gray.copy(0.2f),
                        contentColor = Color(android.graphics.Color.parseColor("#bdec3a"))
                    )
                }) {
                LazyColumn(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .fillMaxSize()
                ) {
                    item {
                        Spacer(modifier = Modifier.size(15.dp))
                        ProfileCard(
                            avatarUri = localeState.data.author.avatar.uri,
                            userName = localeState.data.author.name,
                            userDescription = localeState.data.author.description,
                            onMoreClick = {
                                openBottomSheet.value = true
                            },
                            onEditProfile = {
                                onChangeProfileClick(localeState.data.author)
                            },
                            onAddEvent = {
                                onCreateEventClick()
                            }
                        )

                    }
                    item {
                        Log.d("MyTag", "ProfileScreen: ${localeState.data.authorEvents} ")
                        ProfileRowEventsCard(
                            events = localeState.data.authorEvents,
                            onEventClick = onEventClick,
                            onAllEventClick = {

                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.size(300.dp))
                    }
                }

                if (openBottomSheet.value) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            openBottomSheet.value = false
                        },
                        containerColor = MaterialTheme.colorScheme.background,
                    ) {

                        AnimatedVisibility(
                            visible = openBottomSheet.value,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it })
                        ) {
                            Column {
                                ModalSheetContent(
                                    onCreateEventClick = {
                                        onCreateEventClick()
                                        openBottomSheet.value = false
                                    },
                                    onEditProfileClick = {
                                        onChangeProfileClick(localeState.data.author)
                                        openBottomSheet.value = false
                                    },
                                    onShowFavoriteClick =
                                    {
                                        openBottomSheet.value = false
                                        onShowFavoriteClick()
                                    },
                                    onSignOutClick = {
                                        showAlertDialog.value = true
                                    },
                                    onAdminPanelClick =
                                    {
                                        openBottomSheet.value = false
                                        onAdminClick()
                                    },
                                    showAdminPanel = localeState.data.isAdmin
                                )
                                Spacer(modifier = Modifier.size(80.dp))
                            }
                        }

                    }

                }

                if (showAlertDialog.value) {
                    SignOutAlertDialog(
                        onDismiss = {
                            showAlertDialog.value = false
                        },
                        onSignOutConfirmed = {
                            openBottomSheet.value = false
                            showAlertDialog.value = false
                            onSignInClick()
                            //  viewModel.signOut()

                        }
                    )
                }


            }

        }
    }


}


