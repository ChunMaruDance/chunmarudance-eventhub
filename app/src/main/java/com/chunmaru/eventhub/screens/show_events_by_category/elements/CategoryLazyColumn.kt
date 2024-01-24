package com.chunmaru.eventhub.screens.show_events_by_category.elements

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.data.model.EventAuthorType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryLazyColumn(
    eventAuthorTypes: List<EventAuthorType>,
    onEventClick: (Event) -> Unit,
    onLikeEvent: (Event) -> Unit
) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {

        eventAuthorTypes.forEach { eventAuthor ->
            val event = eventAuthor.event
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
                            end = 5.dp,
                            start = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AsyncImage(
                        model = event.imgUri.uri,
                        contentDescription = "Event Image",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(10))
                            .clickable {
                                onEventClick(event)
                            }
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = event.name, color = Color.White, fontSize = 16.sp)
                        Text(text = event.date, color = Color.Gray, fontSize = 12.sp)
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

                            CategoryModalSheetContent(
                                onShowClick = {
                                    onEventClick(event)
                                    openBottomSheet.value = false
                                },
                                onLikeClick = {
                                    onLikeEvent(event)
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