package com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.R
import com.chunmaru.eventhub.data.model.event.Event

@Composable
fun ProfileRowEventsCard(
    type: String = "My Events",
    events: List<Event>,
    onEventClick: (Event) -> Unit,
    onAllEventClick: () -> Unit
) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = type,
                fontSize = 22.sp,
                color = Color(android.graphics.Color.parseColor("#bdec3a")),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "view all",
                fontSize = 16.sp,
                color =  Color(android.graphics.Color.parseColor("#4d4d4d")),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onAllEventClick()
                }
            )
        }

        Spacer(modifier = Modifier.size(10.dp))
        Log.d("MyTag", "ProfileScreen events: $events ")

        Row(
           modifier = Modifier
               .fillMaxWidth()
               .horizontalScroll(rememberScrollState())
        ) {
            events.forEachIndexed { index, event ->

                    ProfileEventCard(
                        event = event, onClick = onEventClick
                    )
                    Spacer(modifier = Modifier.size(10.dp))

            }

        }
    }


}

@Composable
fun ProfileEventCard(
    event: Event,
    onClick: (Event) -> Unit
) {

    Card(
        modifier = Modifier
            .clickable {
                onClick(event)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

    ) {

        val loader = remember {
            mutableStateOf(true)
        }
        Column {


            if (loader.value) {
                Box(
                    modifier = Modifier.size(160.dp, 190.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

            }
            AsyncImage(
                model = event.imgUri.uri, contentDescription = "Event Image",
                modifier = Modifier
                    .height(190.dp)
                    .width(180.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillBounds,
                onLoading = {
                    loader.value = true
                },
                onSuccess = {
                    loader.value = false
                }
            )
            if (!loader.value) {
                Spacer(modifier = Modifier.size(5.dp))
                Column {
                    Row(
                        modifier = Modifier
                            .width(180.dp)
                            .padding(start = 5.dp, end = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = if (event.name.length > 12) {
                                event.name.take(8) + "..."
                            } else {
                                event.name
                            },
                            color = Color.White,
                            fontSize = 10.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            Text(
                                text = event.date,
                                color = Color(android.graphics.Color.parseColor("#B8B8B8")),
                                fontSize = 10.sp
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.calendar_event),
                                contentDescription = "Clock png",
                                tint = Color(android.graphics.Color.parseColor("#B8B8B8")),
                                modifier = Modifier.size(10.dp)
                            )

                        }

                    }
                }


            }


        }

    }


}





