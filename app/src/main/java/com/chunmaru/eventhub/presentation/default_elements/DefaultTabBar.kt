package com.chunmaru.eventhub.presentation.default_elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
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
fun DefaultTabBar(
    eventTypes: List<EventType>,
    onItemClick: (String) -> Unit
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 7.dp, top = 2.dp)
    ) {

        eventTypes.forEach { type ->

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (type.selected) Color.Gray.copy(alpha = 0.2f)
                        else Color.Transparent
                    ),
                    modifier = Modifier
                        .padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                        .clip(RoundedCornerShape(30))
                        .clickable {
                            onItemClick(type.type)
                        },
                    shape = RoundedCornerShape(35),

                    ) {

                    Text(
                        text = type.type,
                        fontSize = 18.sp,
                        fontWeight = if (type.selected) FontWeight.Bold
                        else FontWeight.Normal,
                        color = if (type.selected) Color(
                            android.graphics.Color.parseColor(
                                "#bdec3a"
                            )
                        ) //
                        else Color(
                            android.graphics.Color.parseColor(
                                "#B8B8B8"
                            )
                        ),
                        modifier = Modifier.padding(10.dp)
                    )

                }

            }

        }


    }


}