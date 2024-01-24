package com.chunmaru.eventhub.screens.admin_screen


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chunmaru.eventhub.R
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.screens.admin_screen.elements.AdminsTabBar
import com.chunmaru.eventhub.screens.admin_screen.elements.CancelDeleteButtons
import com.chunmaru.eventhub.screens.admin_screen.elements.ShowEventCardToAdmin
import com.chunmaru.eventhub.screens.create_event_screen.elements.CustomMultilineHintTextField
import com.chunmaru.eventhub.screens.default_elements.DefaultProgressBar
import com.chunmaru.eventhub.screens.default_elements.DefaultTopBar

@Composable
fun AdminScreen(
    onEventClick: (Event) -> Unit,
    onBackClick: () -> Unit
) {

    val viewModel: AdminScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    val searchTextState = viewModel.searchText.collectAsState()
    val active = remember {
        mutableStateOf(false)
    }
    when (val localeState = state.value) {
        AdminScreenState.Initial -> {}
        AdminScreenState.Pending -> {
            DefaultProgressBar()
        }

        is AdminScreenState.ShowData -> {

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = Color.Transparent,
                topBar = {

                    DefaultTopBar(
                        onBackClick = {
                            onBackClick()
                        },
                        title = "Admin Panel"
                    )
                }
            ) { paddingValues ->

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        val keyboardController = LocalSoftwareKeyboardController.current

                        TextField(
                            value = searchTextState.value,
                            onValueChange = {
                                viewModel.search(it)
                                active.value = it.isNotEmpty()

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, top = 5.dp)
                                .clip(RoundedCornerShape(30)),
                            shape = RoundedCornerShape(30),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(android.graphics.Color.parseColor("#1A1A1A")),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                textColor = Color.White,
                                cursorColor = Color.Gray,
                            ),
                            textStyle = TextStyle(fontSize = 18.sp),
                            placeholder = {
                                Text(text = "Search", color = Color.Gray, fontSize = 18.sp)
                            },
                            trailingIcon = {
                                if (active.value) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        modifier = Modifier
                                            .clickable {
                                                if (searchTextState.value.isNotEmpty())
                                                    viewModel.search("")
                                                else
                                                    keyboardController?.hide()
                                            }
                                            .size(25.dp),
                                        tint = Color(android.graphics.Color.parseColor("#bdec3a")),
                                    )
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.search_icon),
                                    contentDescription = "Search Icon",
                                    tint = Color(android.graphics.Color.parseColor("#bdec3a")),
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        )

                    }

                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            localeState.events.forEach { event ->

                                val expandedState = remember {
                                    mutableStateOf(false)
                                }

                                val reason = remember {
                                    mutableStateOf("")
                                }

                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5))
                                        .animateContentSize(
                                            animationSpec = tween(
                                                delayMillis = 300,
                                                easing = LinearOutSlowInEasing
                                            )
                                        )
                                        .padding(10.dp),
                                    shape = RoundedCornerShape(5),
                                ) {
                                    ShowEventCardToAdmin(
                                        event = event,
                                        onClick = onEventClick,
                                        cornerRadius = RoundedCornerShape(10),
                                        onDeleteClick = {
                                            expandedState.value = !expandedState.value
                                        }
                                    )

                                    if (expandedState.value) {
                                        Spacer(modifier = Modifier.size(5.dp))
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color.Transparent
                                            ),
                                            elevation = CardDefaults.elevatedCardElevation(
                                                defaultElevation = 10.dp
                                            ),
                                            shape = RoundedCornerShape(5),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(5))
                                        ) {
                                            CustomMultilineHintTextField(
                                                value = reason.value,
                                                onValueChanged = {
                                                    reason.value = it
                                                },
                                                hint = "Write your reason",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(5))
                                                    .background(
                                                        Color(
                                                            android.graphics.Color.parseColor(
                                                                "#222222"
                                                            )
                                                        )
                                                    )
                                                    .padding(5.dp),
                                                maxLines = 5,
                                                minLines = 3
                                            )
                                        }
                                        Row {
                                            Button(
                                                onClick = {
                                                    viewModel.deleteEvent(event, reason.value)
                                                    reason.value = ""
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(
                                                        android.graphics.Color.parseColor(
                                                            "#222222"
                                                        )
                                                    )
                                                ),
                                                shape = RoundedCornerShape(25)

                                            ) {
                                                Text(
                                                    text = "Save",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )

                                            }
                                            Spacer(modifier = Modifier.size(5.dp))
                                            Button(
                                                onClick = {
                                                    expandedState.value = false
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(
                                                        android.graphics.Color.parseColor(
                                                            "#222222"
                                                        )
                                                    )
                                                ),
                                                shape = RoundedCornerShape(25)

                                            ) {
                                                Text(
                                                    text = "Cancel",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )

                                            }


                                        }

                                    }


                                }


                            }


                        }
                    }


                    item {
                        Spacer(modifier = Modifier.size(20.dp))
                        val newType = remember {
                            mutableStateOf("")
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp),
                        ) {

                            Text(
                                text = "Type Name",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp, top = 5.dp)
                            )

                            BasicTextField(
                                value = newType.value,
                                onValueChange = {
                                    newType.value = it
                                },
                                textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.background(Color.Transparent),
                                    ) {
                                        if (newType.value.isEmpty()) {
                                            Text(
                                                text = "Write your event name",
                                                color = Color.Gray,
                                                fontSize = 16.sp
                                            )
                                        }
                                        innerTextField()
                                    }
                                },
                                modifier = Modifier.offset(y = (-10).dp)
                            )

                            if (newType.value.isNotEmpty()) {
                                Row {

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.Gray.copy(alpha = 0.2f)
                                        ),
                                        modifier = Modifier
                                            .padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                                            .clip(RoundedCornerShape(20))
                                            .clickable {
                                                newType.value = ""
                                            },
                                        shape = RoundedCornerShape(20),

                                        ) {

                                        Text(
                                            text = "Cancel",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                )
                                            ),
                                            modifier = Modifier.padding(10.dp)
                                        )

                                    }

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.Gray.copy(alpha = 0.2f)
                                        ),
                                        modifier = Modifier
                                            .padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                                            .clip(RoundedCornerShape(20))
                                            .clickable {
                                                viewModel.createType(newType.value)

                                            },
                                        shape = RoundedCornerShape(20),

                                        ) {

                                        Text(
                                            text = "Create",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                )
                                            ),
                                            modifier = Modifier.padding(10.dp)
                                        )

                                    }

                                }

                            }


                        }
                    }

                    item {
                        Spacer(modifier = Modifier.size(10.dp))
                        AdminsTabBar(
                            eventTypes = localeState.types,
                            onItemClick = { category ->
                                viewModel.setCategory(category)
                            }
                        )
                    }

                    item {
                        if (localeState.types.any { it.selected }) {
                            CancelDeleteButtons(
                                cancelClick = {
                                    viewModel.cancelSelectCategory()
                                },
                                deleteClick = {
                                    viewModel.deleteTypes()
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.size(15.dp))
                        val expandedState = remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp)
                                .fillMaxWidth()
                                .animateContentSize(
                                    animationSpec = tween(
                                        delayMillis = 300,
                                        easing = LinearOutSlowInEasing
                                    )
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(android.graphics.Color.parseColor("#1a1a1a"))
                            ),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Popular Categories", fontSize = 20.sp,
                                    color = Color(
                                        android.graphics.Color.parseColor(
                                            "#bdec3a"
                                        )
                                    ),
                                    modifier = Modifier.padding(start = 5.dp)

                                )

                                val rotationState = animateFloatAsState(
                                    targetValue =
                                    if (expandedState.value) 180f
                                    else 0f, label = ""
                                )

                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50)),
                                    shape = RoundedCornerShape(35),

                                    ) {
                                    Row(
                                        modifier = Modifier
                                            .width(45.dp)
                                            .height(45.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .size(35.dp)
                                                .rotate(rotationState.value)
                                                .clickable {
                                                    expandedState.value = !expandedState.value
                                                    if (expandedState.value)
                                                        viewModel.showStatistic()

                                                },
                                            imageVector = Icons.Outlined.ArrowDropDown,
                                            contentDescription = "More categories",
                                            tint = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                )
                                            )

                                        )
                                    }

                                }

                            }
                            if (expandedState.value) {

                                when (localeState.showStatistic) {
                                    ShowStatisticsCategory.Initial -> {}
                                    ShowStatisticsCategory.Pending -> {
                                        DefaultProgressBar()
                                    }

                                    is ShowStatisticsCategory.ShowStatistics -> {
                                        Column(Modifier.padding(start = 5.dp)) {
                                            Text(
                                                text = "Popular", fontSize = 16.sp,
                                                color = Color(
                                                    android.graphics.Color.parseColor(
                                                        "#bdec3a"
                                                    )
                                                )
                                            )
                                            localeState.showStatistic.popularCategory.forEach { categoryCount ->
                                                Text(
                                                    text = "${categoryCount.category} : ${categoryCount.eventCount}",
                                                    fontSize = 14.sp,
                                                    color = Color(
                                                        android.graphics.Color.parseColor("#B8B8B8")
                                                    )
                                                )
                                            }

                                            Text(
                                                text = "Unpopular", fontSize = 16.sp,
                                                color = Color(
                                                    android.graphics.Color.parseColor(
                                                        "#bdec3a"
                                                    )
                                                )
                                            )
                                            localeState.showStatistic.unpopularCategory.forEach { categoryCount ->
                                                Text(
                                                    text = "${categoryCount.category} : ${categoryCount.eventCount}",
                                                    fontSize = 14.sp,
                                                    color = Color(
                                                        android.graphics.Color.parseColor("#B8B8B8")
                                                    )
                                                )
                                            }


                                        }
                                    }
                                }


                            }
                        }


                    }
                    //
                    item {
                        Spacer(modifier = Modifier.size(15.dp))
                        val expandedState = remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp)
                                .fillMaxWidth()
                                .animateContentSize(
                                    animationSpec = tween(
                                        delayMillis = 300,
                                        easing = LinearOutSlowInEasing
                                    )
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(android.graphics.Color.parseColor("#1a1a1a"))
                            ),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Active Authors", fontSize = 20.sp,
                                    color = Color(
                                        android.graphics.Color.parseColor(
                                            "#bdec3a"
                                        )
                                    ),
                                    modifier = Modifier.padding(start = 5.dp)

                                )

                                val rotationState = animateFloatAsState(
                                    targetValue =
                                    if (expandedState.value) 180f
                                    else 0f, label = ""
                                )

                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50)),
                                    shape = RoundedCornerShape(35),

                                    ) {
                                    Row(
                                        modifier = Modifier
                                            .width(45.dp)
                                            .height(45.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .size(35.dp)
                                                .rotate(rotationState.value)
                                                .clickable {
                                                    expandedState.value = !expandedState.value
                                                    if (expandedState.value)
                                                        viewModel.showActiveAuthors()

                                                },
                                            imageVector = Icons.Outlined.ArrowDropDown,
                                            contentDescription = "more authors",
                                            tint = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                )
                                            )

                                        )
                                    }

                                }

                            }
                            if (expandedState.value) {

                                when (localeState.showActiveAuthors) {
                                    ShowActiveAuthors.Initial -> {}
                                    ShowActiveAuthors.Pending -> {
                                        DefaultProgressBar()
                                    }

                                    is ShowActiveAuthors.ShowAuthors -> {
                                        Column(Modifier.padding(start = 5.dp)) {
                                            Text(
                                                text = "Authors", fontSize = 16.sp,
                                                color = Color(
                                                    android.graphics.Color.parseColor(
                                                        "#bdec3a"
                                                    )
                                                )
                                            )
                                            localeState.showActiveAuthors.activeAuthor.forEach { authorCount ->
                                                Text(
                                                    text = "${authorCount.authorName} : ${authorCount.eventCount}",
                                                    fontSize = 14.sp,
                                                    color = Color(
                                                        android.graphics.Color.parseColor("#B8B8B8")
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }


                            }
                        }


                    }//

                    //
                    item {
                        Spacer(modifier = Modifier.size(15.dp))
                        val expandedState = remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp)
                                .fillMaxWidth()
                                .animateContentSize(
                                    animationSpec = tween(
                                        delayMillis = 300,
                                        easing = LinearOutSlowInEasing
                                    )
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(android.graphics.Color.parseColor("#1a1a1a"))
                            ),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Events Reviews", fontSize = 20.sp,
                                    color = Color(
                                        android.graphics.Color.parseColor(
                                            "#bdec3a"
                                        )
                                    ),
                                    modifier = Modifier.padding(start = 5.dp)

                                )

                                val rotationState = animateFloatAsState(
                                    targetValue =
                                    if (expandedState.value) 180f
                                    else 0f, label = ""
                                )

                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Transparent
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50)),
                                    shape = RoundedCornerShape(35),

                                    ) {
                                    Row(
                                        modifier = Modifier
                                            .width(45.dp)
                                            .height(45.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .size(35.dp)
                                                .rotate(rotationState.value)
                                                .clickable {
                                                    expandedState.value = !expandedState.value
                                                    if (expandedState.value)
                                                        viewModel.showReviewsStatistic()

                                                },
                                            imageVector = Icons.Outlined.ArrowDropDown,
                                            contentDescription = "more reviews",
                                            tint = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                )
                                            )

                                        )
                                    }

                                }

                            }
                            if (expandedState.value) {

                                when (localeState.showReviewsStatistics) {
                                    ShowReviewsStatistics.Initial -> {}
                                    ShowReviewsStatistics.Pending -> {
                                        DefaultProgressBar()
                                    }

                                    is ShowReviewsStatistics.ShowReviews -> {
                                        Column(Modifier.padding(start = 5.dp)) {
                                            Text(
                                                text = "Reviews", fontSize = 16.sp,
                                                color = Color(
                                                    android.graphics.Color.parseColor(
                                                        "#bdec3a"
                                                    )
                                                )
                                            )
                                            localeState.showReviewsStatistics.reviews.forEach { reviews ->
                                                Text(
                                                    text = "${reviews.eventName} : ${reviews.reviewCount}",
                                                    fontSize = 14.sp,
                                                    color = Color(
                                                        android.graphics.Color.parseColor("#B8B8B8")
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }


                            }
                        }


                    }//

                    item { Spacer(modifier = Modifier.size(90.dp)) }


                }


            }

        }


    }
}

