package com.chunmaru.eventhub.presentation.screens.admin_screen.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chunmaru.eventhub.data.model.event.EventType

@Composable
fun AdminsTabBar(
    eventTypes: List<EventType>,
    onItemClick: (String) -> Unit
) {

    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        modifier = Modifier
            .padding(start = 7.dp, top = 2.dp)
            .height(175.dp)
    ) {

        eventTypes.forEach { eventsType ->
            item(key = eventsType.type) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (eventsType.selected) Color.Gray.copy(alpha = 0.2f)
                        else Color.Transparent
                    ),
                    modifier = Modifier
                        .padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                        .clip(RoundedCornerShape(25))
                        .clickable {
                            onItemClick(eventsType.type)
                        },
                    shape = RoundedCornerShape(25),
                ) {
                    Text(
                        text = eventsType.type,
                        fontSize = 18.sp,
                        fontWeight = if (eventsType.selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (eventsType.selected) Color(android.graphics.Color.parseColor("#bdec3a"))
                        else Color(android.graphics.Color.parseColor("#B8B8B8")),
                        modifier = Modifier.padding(10.dp),
                        maxLines = 1,
                    )
                }
            }
        }

    }

}