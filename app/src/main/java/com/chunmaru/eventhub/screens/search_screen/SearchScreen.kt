package com.chunmaru.eventhub.screens.search_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chunmaru.eventhub.R
import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.screens.default_elements.DefaultDropDownMenuItems
import com.chunmaru.eventhub.screens.default_elements.DefaultProgressBar
import com.chunmaru.eventhub.screens.search_screen.elements.ShowAuthorCard
import com.chunmaru.eventhub.screens.search_screen.elements.ShowEventCard

@Composable
fun SearchScreen(
    onEventClick: (Event) -> Unit,
    onAuthorClick: (Author) -> Unit
) {

    val viewModel: SearchScreenViewModel = hiltViewModel()
    val state = viewModel.combinedState.collectAsState()

    SearchScreenContent(
        state = state.value,
        onEventClick = onEventClick,
        onAuthorClick = onAuthorClick,
        onSearch = viewModel::search,
        onCriterionClick = viewModel::setCriterion
    )

}

@Composable
private fun SearchScreenContent(
    state: SearchScreenCombineState,
    onEventClick: (Event) -> Unit,
    onAuthorClick: (Author) -> Unit,
    onSearch: (String) -> Unit,
    onCriterionClick: (String) -> Unit
) {
    val active = remember {
        mutableStateOf(false)
    }

    val searchText = state.searchText
    val searchCriteria = state.searchCriteria

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            TextField(
                value = searchText,
                onValueChange = {
                    onSearch(it)
                    active.value = it.isNotEmpty()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                    .clip(RoundedCornerShape(50))
                    .onFocusChanged {
                        if (it.isFocused) active.value = true
                    },

                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        active.value = searchText.isNotEmpty()
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(android.graphics.Color.parseColor("#1A1A1A")),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.Gray,
                ),
                textStyle = TextStyle(fontSize = 18.sp),
                maxLines = 1,
                placeholder = {
                    Text(text = "Search", color = Color.Gray, fontSize = 18.sp)
                },
                trailingIcon = {
                    val expanded = remember {
                        mutableStateOf(false)
                    }
                    if (active.value) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                IconButton(onClick = {
                                    expanded.value = !expanded.value
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.setting_lines),
                                        contentDescription = "Settings",
                                        modifier = Modifier
                                            .size(20.dp),
                                        tint = Color(android.graphics.Color.parseColor("#bdec3a")),
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded.value,
                                    onDismissRequest = {
                                        expanded.value = false
                                    },
                                    modifier = Modifier.background(
                                        Color(
                                            android.graphics.Color.parseColor("#1A1A1A")
                                        )
                                    )
                                ) {

                                    DefaultDropDownMenuItems(
                                        items = searchCriteria,
                                        onItemCheckChange = onCriterionClick
                                    )

                                }

                            }
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                modifier = Modifier
                                    .clickable {
                                        if (searchText.isNotEmpty())
                                            onSearch("")
                                        else {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }

                                    }
                                    .size(30.dp),
                                tint = Color(android.graphics.Color.parseColor("#bdec3a")),
                            )
                            Spacer(modifier = Modifier.size(7.dp))
                        }

                    }

                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search_icon),
                        contentDescription = "Search Icon",
                        tint = Color(android.graphics.Color.parseColor("#bdec3a")),
                        modifier = Modifier.size(25.dp)
                    )
                },
            )
        }

        when (val currentState = state.searchScreenState) {
            SearchScreenState.Initial -> {}

            SearchScreenState.Pending -> {
                item { DefaultProgressBar() } }

            is SearchScreenState.SearchResult -> {

                currentState.events.forEach { event ->
                    item(key = event.id) {
                        ShowEventCard(
                            event = event,
                            onClick = onEventClick,
                            cornerRadius = RoundedCornerShape(10)
                        )
                    }


                }

                currentState.authors.forEach { author ->
                    item(key = author.id) {
                        ShowAuthorCard(
                            author = author,
                            onClick = onAuthorClick,
                            cornerRadius = RoundedCornerShape(50)
                        )
                    }

                }
                item {
                    Spacer(modifier = Modifier.size(80.dp))
                }


            }
        }


    }


}
