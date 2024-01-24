package com.chunmaru.eventhub.presentation.screens.comments_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.reviews.ReviewAuthor
import com.chunmaru.eventhub.presentation.screens.create_event_screen.elements.CustomMultilineHintTextField
import com.chunmaru.eventhub.presentation.default_elements.DefaultProgressBar
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import com.chunmaru.eventhub.presentation.screens.comments_screen.elements.CommentCard
import com.chunmaru.eventhub.presentation.screens.comments_screen.elements.CommentTopBar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun CommentScreen(
    event: Event,
    onBackClick: () -> Unit
) {

    val viewModel: CommentsScreenViewModel = hiltViewModel()
    viewModel.setEvent(event = event)

    val state = viewModel.state.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CommentTopBar(
                onBackClick = onBackClick,
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        when (val localeState = state.value) {
            is ScreenState.Initial -> {}

            is ScreenState.Pending -> {
                Box(modifier = Modifier.padding(top = 60.dp)) {
                    DefaultProgressBar()
                }

            }

            is ScreenState.Success -> {
                Content(
                    reviews = localeState.data.reviews,
                    isAuthor = localeState.data.isAuthor,
                    viewModel = viewModel
                )

            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    reviews: List<ReviewAuthor>,
    isAuthor: Boolean,
    viewModel: CommentsScreenViewModel,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 75.dp, start = 10.dp, end = 10.dp)
    ) {
        items(reviews) {

            key(it.reviewId) {
                val dismissState = rememberDismissState()

                val swiped = isAuthor || it.auhtor.id == Firebase.auth.uid

                if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                    viewModel.deleteComment(it)
                }

                SwipeToDismiss(
                    state = dismissState,
                    directions = if (swiped) setOf(DismissDirection.EndToStart) else setOf(),
                    background = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Transparent)
                        )
                    },
                    dismissContent = {
                        CommentCard(it)
                    },
                )
            }
        }

        item {
            val message = remember {
                mutableStateOf("")
            }

            val showDialog = remember { mutableStateOf(false) }

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog.value = false
                    },
                    title = {
                        Text(
                            text = "Do you really want to send a comment?",
                            color = Color(
                                android.graphics.Color.parseColor(
                                    "#bdec3a"
                                )
                            )
                        )
                    },
                    text = {
                        Text(
                            text = message.value,
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
                                    showDialog.value = false
                                }
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = Color(
                                        android.graphics.Color.parseColor(
                                            "#bdec3a"
                                        )
                                    )
                                )
                            }

                            TextButton(
                                onClick = {
                                    showDialog.value = false
                                    viewModel.addReview(message.value)
                                    message.value = ""
                                }
                            ) {
                                Text(
                                    text = "Send",
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

            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp),
                horizontalAlignment = Alignment.End
            ) {
                CustomMultilineHintTextField(
                    value = message.value,
                    onValueChanged = {
                        if (it.length < 1500)
                            message.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5))
                        .background(Color(android.graphics.Color.parseColor("#1A1A1A")))
                        .padding(5.dp),
                    hint = "Write your comment"
                )

                Button(
                    onClick = {
                        showDialog.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(android.graphics.Color.parseColor("#1A1A1A"))
                    ),
                    shape = RoundedCornerShape(30)

                ) {
                    Text(
                        text = "Save",
                        fontSize = 16.sp,
                        color = Color(
                            android.graphics.Color.parseColor(
                                "#bdec3a"
                            )
                        )
                    )

                }


            }
        }


        item { Spacer(modifier = Modifier.size(100.dp)) }


    }


}
