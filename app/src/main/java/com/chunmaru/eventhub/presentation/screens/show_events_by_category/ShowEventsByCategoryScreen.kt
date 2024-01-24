package com.chunmaru.eventhub.presentation.screens.show_events_by_category


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.presentation.default_elements.DefaultProgressBar
import com.chunmaru.eventhub.presentation.default_elements.DefaultTopBarWithArrowBack
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import com.chunmaru.eventhub.presentation.screens.show_events_by_category.elements.CategoryLazyColumn


@Composable
fun ShowEventsByCategoryScreen(
    category: String,
    onEventClick: (Event) -> Unit,
    onBackClick: () -> Unit
) {

    val viewModel: ShowEventsByCategoryScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()
    viewModel.setCategory(category)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.Transparent,
        topBar = {

            DefaultTopBarWithArrowBack(
                title = category,
                onBackClick = onBackClick
            )

        }
    ) { paddingValues ->
        when (val currentState = state.value) {
            is ScreenState.Initial -> {}

            is ScreenState.Pending -> {
                DefaultProgressBar()
            }

            is ScreenState.Success -> {

                CategoryLazyColumn(
                    currentState.data.events,
                    onEventClick = onEventClick,
                    onLikeEvent = viewModel::likeEvent
                )

            }
        }
    }


}