package com.chunmaru.eventhub.screens.show_event_screen.element

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

@Composable
fun ShowEventTabBar(
    types: List<String>,
    onItemClick: () -> Unit
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 7.dp, top = 10.dp)
    ) {

        types.forEach { type ->

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                        .clip(RoundedCornerShape(30))
                        .clickable {
                            onItemClick()
                        },
                    shape = RoundedCornerShape(35),

                    ) {

                    Text(
                        text = type,
                        fontSize = 20.sp,
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