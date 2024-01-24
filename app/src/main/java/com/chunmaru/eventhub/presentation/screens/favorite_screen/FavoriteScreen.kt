package com.chunmaru.eventhub.presentation.screens.favorite_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.presentation.default_elements.DefaultProgressBar
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import com.chunmaru.eventhub.presentation.screens.favorite_screen.elements.FavoriteModalSheetContent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    onEventClick: (Event) -> Unit
) {
    val viewModel: FavoriteScreenViewModel = hiltViewModel()
    viewModel.loadFavorite()

    val state = viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 7.dp)
                    .clip(RoundedCornerShape(10))
                    .background(
                        Color(android.graphics.Color.parseColor("#262626")).copy(
                            alpha = 0.4f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Liked Event", fontSize = 26.sp,
                    color = Color(
                        android.graphics.Color.parseColor(
                            "#bdec3a"
                        )
                    ),
                    modifier = Modifier.padding(10.dp)
                )
            }

        }
    ) { paddingValues ->

        val isLoading = viewModel.isLoading.collectAsState()
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading.value)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.loadFavoriteEvents()
            },
            indicator = { indicatorState, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = indicatorState, refreshTriggerDistance = refreshTrigger,
                    backgroundColor = Color.Gray.copy(0.2f),
                    contentColor = Color(android.graphics.Color.parseColor("#bdec3a"))
                )
            },
            modifier = Modifier.padding(top = 63.dp)
        ) {

            when (val localeState = state.value) {
                is ScreenState.Initial -> {}

                is ScreenState.Pending -> {
                    DefaultProgressBar()
                }

                is ScreenState.Success -> {

                    LazyColumn {
                        localeState.data.forEach { event ->
                            Log.d("MyTag", "FavoriteScreen: ${event.id} ")
                            item(key = event.id) {
                                val openBottomSheet = rememberSaveable {
                                    mutableStateOf(false)
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 10.dp,
                                            bottom = 10.dp,
                                            start = 10.dp,
                                            end = 10.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(70.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val loading = remember {
                                            mutableStateOf(false)
                                        }
                                        AsyncImage(
                                            model = event.imgUri.uri,
                                            contentDescription = "Event Image",
                                            contentScale = ContentScale.FillBounds,
                                            modifier = Modifier
                                                .size(70.dp)
                                                .clip(RoundedCornerShape(10))
                                                .clickable {
                                                    onEventClick(event)
                                                },
                                            onLoading = {
                                                loading.value = true
                                            },
                                            onSuccess = {
                                                loading.value = false
                                            }
                                        )
                                        if (loading.value) {
                                            CircularProgressIndicator(
                                                color = Color.White,
                                                strokeWidth = 1.dp,
                                                modifier = Modifier.size(15.dp)
                                            )
                                        }

                                    }

                                    Spacer(modifier = Modifier.size(10.dp))
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = event.name,
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = event.date,
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )

                                    }

                                    IconButton(onClick = {
                                        openBottomSheet.value = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.MoreVert,
                                            contentDescription = "More Icon",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(25.dp)
                                        )
                                    }

                                    if (openBottomSheet.value) {
                                        ModalBottomSheet(
                                            onDismissRequest = {
                                                openBottomSheet.value = false
                                            },
                                            containerColor = MaterialTheme.colorScheme.background,
                                        ) {
                                            FavoriteModalSheetContent(
                                                onDeleteFromFavoriteClick = {
                                                    viewModel.removeFromFavorite(event)
                                                    openBottomSheet.value = false
                                                },
                                                onShowClick = {

                                                    onEventClick(event)
                                                    openBottomSheet.value = false
                                                }
                                            )
                                            Spacer(modifier = Modifier.size(80.dp))

                                        }

                                    }


                                }


                            }

                        }

                        item { Spacer(modifier = Modifier.size(100.dp)) }

                    }


                }

            }


        }

    }


}