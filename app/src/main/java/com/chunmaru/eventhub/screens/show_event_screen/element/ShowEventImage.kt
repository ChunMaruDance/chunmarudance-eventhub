package com.chunmaru.eventhub.screens.show_event_screen.element


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.R
import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event


@Composable
fun BoxScope.ShowEventImage(
    event: Event,
    author: Author,
) {

    val loader = remember {
        mutableStateOf(false)
    }

    AsyncImage(
        model = event.imgUri.uri,
        contentDescription = "Event Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        onLoading = {
            loader.value = true
        },
        onSuccess = {
            loader.value = false
        }
    )



    if (loader.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 20.dp),
                color = Color(android.graphics.Color.parseColor("#bdec3a"))
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background.copy(0.6f)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            Pair(0.8f, Color.Transparent),
                            Pair(1f, MaterialTheme.colorScheme.background)
                        )
                    )
                )
        )

        androidx.compose.material.Card(
            backgroundColor = Color.Transparent,
            shape = RoundedCornerShape(5),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 10.dp)
        ) {
            AsyncImage(
                model = event.imgUri.uri,
                contentDescription = "Event Image",
                modifier = Modifier.size(250.dp),
                contentScale = ContentScale.FillBounds,
            )
        }


        Column(
            Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                text = event.name,
                color = Color(
                    android.graphics.Color.parseColor(
                        "#bdec3a"
                    )
                ),
                fontSize = if (event.name.length > 20) 20.sp else 24.sp,
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 7.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                AsyncImage(
                    model = author.avatar.uri,
                    contentDescription = "author avatar",
                    modifier = Modifier
                        .size(25.dp)
                        .clip(RoundedCornerShape(50)),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = author.name,
                    fontSize = 12.sp,
                    color = Color(
                        android.graphics.Color.parseColor(
                            "#B8B8B8"
                        )
                    )
                )

            }

        }
        Column(
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    event.date, color = Color.Gray, fontSize = 10.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.calendar_event),
                    contentDescription = "Edit icon",
                    tint = Color(
                        android.graphics.Color.parseColor(
                            "#bdec3a"
                        ),
                    ),
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 3.dp, end = 5.dp)
                )


            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    event.time, color = Color.Gray, fontSize = 10.sp
                )
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "Edit icon",
                    tint = Color(
                        android.graphics.Color.parseColor(
                            "#bdec3a"
                        ),
                    ),
                    modifier = Modifier
                        .size(22.dp)
                        .padding(start = 3.dp, end = 5.dp)
                )


            }


        }

    }


}