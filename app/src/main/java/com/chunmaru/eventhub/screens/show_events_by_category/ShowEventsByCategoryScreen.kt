package com.chunmaru.eventhub.screens.show_events_by_category


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
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
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
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.screens.default_elements.DefaultProgressBar
import com.chunmaru.eventhub.screens.default_elements.DefaultTopBarWithArrowBack
import com.chunmaru.eventhub.screens.show_events_by_category.elements.CategoryLazyColumn
import com.chunmaru.eventhub.screens.show_events_by_category.elements.CategoryModalSheetContent

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
            ShowEventsByCategoryScreenState.Initial -> {}

            ShowEventsByCategoryScreenState.Pending -> {
                DefaultProgressBar()
            }

            is ShowEventsByCategoryScreenState.ShowEvents -> {

                CategoryLazyColumn(
                    currentState.events.events,
                    onEventClick = onEventClick,
                    onLikeEvent = viewModel::likeEvent
                )

            }
        }
    }


}