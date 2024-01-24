package com.chunmaru.eventhub.presentation.screens.show_event_screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chunmaru.eventhub.R
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.event.ShowAuthorEvent
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import com.chunmaru.eventhub.presentation.screens.show_event_screen.element.ShowEventImage
import com.chunmaru.eventhub.presentation.screens.show_event_screen.element.ShowEventTabBar


@Composable
fun ShowEventScreen(
    event: Event,
    onBackClick: () -> Unit,
    onCommentClick: (Event) -> Unit
) {

    val viewModel: ShowEventScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.setEvent(event)
    }

    when (val currentState = state.value) {

        is ScreenState.Initial -> {}

        is ScreenState.Pending -> {}

        is ScreenState.Success -> {

            ShowEventContent(
                showEventState = currentState,
                onBackClick = onBackClick,
                onLikeClick = viewModel::likeEvent,
                onCommentClick = onCommentClick
            )

        }
    }

}

@Composable
private fun ShowEventContent(
    showEventState: ScreenState.Success<ShowAuthorEvent>,
    onBackClick: () -> Unit,
    onLikeClick: (String) -> Unit,
    onCommentClick: (Event) -> Unit
) {

    val event = showEventState.data.event
    val author = showEventState.data.author
    val favorite = showEventState.data.favorite
    val isHeld = showEventState.data.isHeld

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp)
            ) {

                ShowEventImage(
                    event = event,
                    author = author
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.Start
                ) {
                    androidx.compose.material.IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "back btn",
                            tint = Color(
                                android.graphics.Color.parseColor(
                                    "#B8B8B8"
                                )
                            ),
                            modifier = Modifier
                                .size(35.dp)

                        )
                    }


                }

            }

        }
        item {
            ShowEventTabBar(event.categories,
                onItemClick = {})
        }

        item {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .padding(end = 10.dp, top = 5.dp, bottom = 5.dp, start = 10.dp)
                        .clip(RoundedCornerShape(30))
                        .clickable {
                            //
                        },
                    shape = RoundedCornerShape(35),

                    ) {

                    Text(
                        text = event.city,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(
                            android.graphics.Color.parseColor(
                                "#bdec3a"
                            )
                        ),
                        modifier = Modifier.padding(10.dp)
                    )

                }

                Row {
                    if (isHeld) {
                        androidx.compose.material.IconButton(onClick = {
                            onCommentClick(event)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.comment),
                                contentDescription = "comments btn",
                                tint =
                                Color(
                                    android.graphics.Color.parseColor(
                                        "#B8B8B8"
                                    )
                                ),
                                modifier = Modifier
                                    .size(30.dp)

                            )
                        }
                    }

                    androidx.compose.material.IconButton(onClick = {
                        onLikeClick(event.id)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = "like btn",
                            tint = if (favorite) Color(
                                android.graphics.Color.parseColor(
                                    "#bdec3a"
                                ),
                            )
                            else Color(
                                android.graphics.Color.parseColor(
                                    "#B8B8B8"
                                )
                            ),
                            modifier = Modifier
                                .size(35.dp)

                        )
                    }
                }
            }

        }

        item {

            Text(
                text = event.description,
                color = Color(
                    android.graphics.Color.parseColor(
                        "#B8B8B8"
                    )
                ),
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

        }

        item {
            Spacer(modifier = Modifier.size(100.dp))
        }

    }

}



