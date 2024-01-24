package com.chunmaru.eventhub.screens.home_screen.home_screen_element

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event
import com.chunmaru.eventhub.data.model.EventAuthorType

@Composable
fun HomeRowCards(
    type: String,
    events: List<EventAuthorType>,
    onClick: (Event) -> Unit,
    onAllEventClick: (type: String) -> Unit
) {
    Log.d("MyTag", "HomeRowCards: $type ")
    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = type,
                fontSize = 22.sp,
                color = Color(android.graphics.Color.parseColor("#bdec3a")),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp)
            )
            Text(
                text = "view all",
                fontSize = 16.sp,
                color = Color(android.graphics.Color.parseColor("#4d4d4d")),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .clickable {
                        onAllEventClick(type)
                    }

            )
        }

        LazyRow(
            modifier = Modifier
                .padding(start = 5.dp)
                .fillMaxWidth()
        ) {
            events.forEach { event ->
                item(key = event.event.id) {
                    HomeCard(
                        event = event.event,
                        author = event.author,
                        onClick = onClick,
                    )
                }


            }

        }
    }


}




