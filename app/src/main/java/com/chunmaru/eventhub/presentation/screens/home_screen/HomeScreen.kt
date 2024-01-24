package com.chunmaru.eventhub.presentation.screens.home_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.event.EventType
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import com.chunmaru.eventhub.presentation.screens.home_screen.home_screen_element.HomeRowCards
import com.chunmaru.eventhub.presentation.screens.home_screen.home_screen_element.HomeTabBar
import com.chunmaru.eventhub.presentation.screens.home_screen.home_screen_element.HomeTopBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(
    onEventClick: (Event) -> Unit,
    onAllEventClick: (type: String) -> Unit,
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeTopBar()
        },
        containerColor = Color.Transparent
    ) { paddingValues ->

        Box(modifier = Modifier.padding(top = 50.dp)) {

            when (val localeState = state.value) {
                is ScreenState.Initial -> {}

                is ScreenState.Pending -> {
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

                is ScreenState.Success -> {
                    val isLoading = viewModel.isLoading.collectAsState()
                    val swipeRefreshState =
                        rememberSwipeRefreshState(isRefreshing = isLoading.value)
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
                                .fillMaxSize()
                                .background(Color.Transparent)
                                .padding(start = 7.dp, top = 25.dp)
                        ) {

                            item {

                                val categories = localeState.data.map {
                                    EventType(
                                        selected = it.selected,
                                        type = it.eventsCategories.category
                                    )
                                }

                                HomeTabBar(
                                    types = categories,
                                    onCategoriesClick = { selectedType ->
                                        viewModel.clickOnCategories(selectedType)
                                    },
                                )

                            }
                            items(items = localeState.data) { type ->
                                AnimatedVisibility(
                                    visible = type.selected,
                                    enter = slideInHorizontally(
                                        initialOffsetX = { fullWidth -> fullWidth },
                                        animationSpec = tween(400)
                                    ),
                                    exit = slideOutHorizontally(
                                        targetOffsetX = { fullWidth -> fullWidth },
                                        animationSpec = tween(400)
                                    )
                                ) {

                                    HomeRowCards(
                                        type = type.eventsCategories.category,
                                        events = type.eventsCategories.events,
                                        onClick = onEventClick,
                                        onAllEventClick = onAllEventClick
                                    )
                                }

                            }

                        }

                    }


                }
            }


        }


    }


}