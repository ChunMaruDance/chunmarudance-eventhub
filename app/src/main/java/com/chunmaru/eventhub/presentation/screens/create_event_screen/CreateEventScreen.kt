package com.chunmaru.eventhub.presentation.screens.create_event_screen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.chunmaru.eventhub.R
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.presentation.screens.create_event_screen.elements.CityDropdownMenu
import com.chunmaru.eventhub.presentation.screens.create_event_screen.elements.CustomMultilineHintTextField
import com.chunmaru.eventhub.presentation.default_elements.DefaultTabBar
import com.chunmaru.eventhub.presentation.default_elements.state.ScreenState
import com.chunmaru.eventhub.presentation.screens.create_event_screen.elements.CreateEventCard
import com.chunmaru.eventhub.presentation.screens.create_event_screen.elements.EventTopBar
import com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element.ModalProfileSheetContent
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    onBackClick: () -> Unit,
    onCommentsClick: (Event) -> Unit,
    event: Event
) {
    val viewModel: CreateEventScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.setUp(event)
    }


    val openBottomSheet = rememberSaveable {
        mutableStateOf(false)
    }


    when (val locateState = state.value) {
        is ScreenState.Success -> {

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp),
                containerColor = Color.Transparent,
                topBar = {
                    EventTopBar(
                        onBackClick = onBackClick,
                        onButtonClick = {
                            viewModel.saveEvent(
                                onSuccess = onBackClick,
                                onError = {

                                }
                            )
                        },
                        mainText = "Create Event",
                        buttonText = "Save",

                        )
                }
            ) { paddingValues ->

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                        ) {
                            CreateEventCard(
                                imageUri = locateState.data.event.imgUri.uri,
                                onChange = {
                                    viewModel.setAvatar(it)
                                }
                            )

                        }
                    }

                    item {
                        DefaultTabBar(
                            eventTypes = locateState.data.allTypes,
                            onItemClick = {
                                viewModel.setCategories(it)
                            }
                        )
                    }

                    item {

                        val dateDialogState = rememberMaterialDialogState()
                        val timeDialogState = rememberMaterialDialogState()



                        if (showDialog.value) {
                            AlertDialog(
                                onDismissRequest = {
                                    showDialog.value = false
                                },
                                title = {
                                    Text(
                                        text = "Do you really want to delete event?",
                                        color = Color(
                                            android.graphics.Color.parseColor(
                                                "#bdec3a"
                                            )
                                        )
                                    )
                                },
                                text = {
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
                                                viewModel.deleteEvent(
                                                    onSuccess = {
                                                        onBackClick()
                                                    },
                                                    onError = {

                                                    }
                                                )

                                            }
                                        ) {
                                            Text(
                                                text = "Confirm",
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
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {


                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (event.id.isNotEmpty()) {
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        IconButton(onClick = {
                                            //todo
                                            openBottomSheet.value = !openBottomSheet.value
                                        }) {

                                            Icon(
                                                painter = painterResource(id = R.drawable.more_img),
                                                contentDescription = "more img",
                                                tint = Color(
                                                    android.graphics.Color.parseColor(
                                                        "#bdec3a"
                                                    )
                                                ),
                                                modifier = Modifier.size(30.dp)

                                            )
                                        }
                                        Text(
                                            text = "More",
                                            color = Color.Gray
                                        )

                                    }
                                }


                                Text(
                                    text = locateState.data.event.date,
                                    color = Color.Gray
                                )
                                IconButton(onClick = {
                                    dateDialogState.show()
                                }) {

                                    Icon(
                                        painter = painterResource(id = R.drawable.calendar_event),
                                        contentDescription = "Edit icon",
                                        tint = Color(
                                            android.graphics.Color.parseColor(
                                                "#bdec3a"
                                            ),
                                        ),
                                        modifier = Modifier.size(30.dp)
                                    )

                                }

                            }//

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        locateState.data.event.time, color = Color.Gray
                                    )
                                    IconButton(onClick = {
                                        timeDialogState.show()
                                    }) {

                                        Icon(
                                            painter = painterResource(id = R.drawable.clock),
                                            contentDescription = "Edit icon",
                                            tint = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                ),
                                            ),
                                            modifier = Modifier.size(30.dp)
                                        )

                                    }

                                }


                            }

                            CityDropdownMenu(
                                cities = listOf(
                                    "Kyiv",
                                    "Kharkiv",
                                    "Odesa",
                                    "Dnipro",
                                    "Donetsk",
                                    "Zaporizhzhia",
                                    "Lviv",
                                    "Kryvyi Rih",
                                    "Mykolaiv",
                                    "Mariupol",
                                    "Luhansk",
                                    "Makiivka",
                                    "Vinnytsia",
                                    "Simferopol",
                                    "Kherson",
                                    "Poltava",
                                    "Chernihiv",
                                    "Cherkasy",
                                    "Sumy",
                                    "Zhytomyr",
                                    "Khmelnytskyi",
                                    "Rivne",
                                    "Ivano-Frankivsk",
                                    "Ternopil"
                                ),
                                selectedCity = locateState.data.event.city,
                                onCitySelected = {
                                    viewModel.setCity(it)
                                }
                            )


                            MaterialDialog(
                                dialogState = dateDialogState,
                                onCloseRequest = {
                                },
                                buttons = {
                                    positiveButton(
                                        text = "Ok", textStyle = TextStyle(
                                            color = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                ),
                                            )
                                        )
                                    ) {

                                    }
                                    negativeButton(
                                        text = "Cancel", textStyle = TextStyle(
                                            color = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                ),
                                            )
                                        )
                                    ) {

                                    }
                                }
                            ) {
                                datepicker(
                                    initialDate = LocalDate.now(),
                                    title = "Pick a date",
                                    colors = com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults.colors(
                                        headerBackgroundColor = Color.White,
                                        headerTextColor = Color(
                                            android.graphics.Color.parseColor(
                                                "#bdec3a"
                                            ),
                                        ),
                                        dateActiveBackgroundColor = Color(
                                            android.graphics.Color.parseColor(
                                                "#bdec3a"
                                            ),
                                        ),
                                    )
                                ) {
                                    viewModel.setDate(it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                }

                            }


                            MaterialDialog(
                                dialogState = timeDialogState,
                                onCloseRequest = {
                                },
                                buttons = {
                                    positiveButton(
                                        text = "Ok", textStyle = TextStyle(
                                            color = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                ),
                                            )
                                        )
                                    ) {

                                    }
                                    negativeButton(
                                        text = "Cancel", textStyle = TextStyle(
                                            color = Color(
                                                android.graphics.Color.parseColor(
                                                    "#bdec3a"
                                                ),
                                            )
                                        )
                                    ) {

                                    }
                                }
                            ) {
                                timepicker(
                                    initialTime = LocalTime.now(),
                                    title = "Pick a date",
                                    colors = TimePickerDefaults.colors(
                                        activeBackgroundColor = Color(
                                            android.graphics.Color.parseColor(
                                                "#bdec3a"
                                            ),
                                        ),
                                        inactiveBackgroundColor = Color.White,
                                        selectorColor = Color(
                                            android.graphics.Color.parseColor(
                                                "#bdec3a"
                                            ),
                                        ),

                                        )
                                ) {
                                    viewModel.setTime(it.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                                }

                            }


                        }
                    }


                    item {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp),
                        ) {

                            Text(
                                text = "Event Name",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp, top = 5.dp)
                            )

                            BasicTextField(
                                value = locateState.data.event.name,
                                onValueChange = {
                                    if (it.length < 52) viewModel.setEventName(it)
                                },
                                textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.background(Color.Transparent),
                                    ) {
                                        if (locateState.data.event.name.isEmpty()) {
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
                        }


                    }

                    item {
                        Text(
                            text = "Event Description",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp, top = 5.dp, start = 15.dp)
                        )
                    }


                    item {

                        Box(modifier = Modifier.padding(horizontal = 10.dp)) {
                            CustomMultilineHintTextField(
                                value = locateState.data.event.description,
                                onValueChanged = {
                                    if (it.length < 5000)
                                        viewModel.setEventDescription(it)
                                },
                                hint = "Write your event description",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(5))
                                    .background(Color(android.graphics.Color.parseColor("#1A1A1A")))
                                    .padding(5.dp),
                                maxLines = 3
                            )
                        }

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
                                ModalProfileSheetContent(
                                    onCommentClick = {
                                        openBottomSheet.value = false
                                        onCommentsClick(locateState.data.event)
                                    },
                                    onDeleteClick = {
                                        openBottomSheet.value = false
                                        showDialog.value = true
                                    })
                                Spacer(modifier = Modifier.size(80.dp))
                            }
                        }

                    }

                }


            }

        }

        is ScreenState.Initial -> {

        }

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
    }


}