package com.chunmaru.eventhub.presentation.screens.home_screen.home_screen_element


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.Event

@Composable
fun HomeCard(
    event: Event,
    author: Author,
    onClick: (Event) -> Unit,
) {

    val loading = remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .padding(end = 10.dp)
            .clickable {
                onClick(event)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

    ) {

        if (loading.value) {
            Box(
                modifier = Modifier
                    .height(230.dp)
                    .width(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp),
                    color = Color(android.graphics.Color.parseColor("#bdec3a"))
                )
            }
        }

        Column {

            AsyncImage(
                model = event.imgUri.uri, contentDescription = "Event Image",
                modifier = Modifier
                    .height(230.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillBounds,
                onLoading = {
                    loading.value = true
                },
                onSuccess = {
                    loading.value = false
                }
            )

            if (!loading.value) {
                Column {
                    Row(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(start = 5.dp, end = 5.dp, top = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (event.name.length >= 26) {
                                event.name.take(23) + "..."
                            } else {
                                event.name
                            },
                            color = Color.White,
                            fontSize = 12.sp
                        )


                    }

                    Row(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(start = 5.dp, end = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                            Text(
                                text = if (author.name.length >= 26) {
                                    author.name.take(23) + "..."
                                } else {
                                    author.name
                                },
                                color = Color(android.graphics.Color.parseColor("#B8B8B8")),
                                fontSize = 12.sp
                            )

                    }

                }
            }


        }

    }

}


