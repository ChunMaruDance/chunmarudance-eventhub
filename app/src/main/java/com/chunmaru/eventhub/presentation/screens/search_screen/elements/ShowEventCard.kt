package com.chunmaru.eventhub.presentation.screens.search_screen.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.data.model.event.Event

@Composable

fun ShowEventCard(
    event: Event,
    cornerRadius: RoundedCornerShape,
    onClick: (Event) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onClick(event)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = event.imgUri.uri,
            contentDescription = "Event Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(60.dp)
                .clip(cornerRadius)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = event.name, color = Color.White, fontSize = 16.sp)
            Text(text = event.date, color = Color.Gray, fontSize = 12.sp)

        }
    }

}