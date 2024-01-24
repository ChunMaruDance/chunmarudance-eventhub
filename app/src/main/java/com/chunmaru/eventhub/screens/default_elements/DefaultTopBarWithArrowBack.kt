package com.chunmaru.eventhub.screens.default_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultTopBarWithArrowBack(
    title: String,
    onBackClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 7.dp)
            .clip(RoundedCornerShape(10))
            .background(
                Color(android.graphics.Color.parseColor("#262626")).copy(
                    alpha = 0.4f
                )
            ),
    ) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray.copy(alpha = 0.2f)
            ),
            modifier = Modifier
                .padding(end = 10.dp, start = 5.dp, top = 5.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(30))
                .clickable {
                    onBackClick()
                },
            shape = RoundedCornerShape(35),

            ) {


            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "ArrowBack",
                tint = Color(
                    android.graphics.Color.parseColor(
                        "#bdec3a"
                    )
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(45.dp)
                    .padding(5.dp)

            )

        }
        Text(
            text = title, fontSize = 26.sp,
            color = Color(
                android.graphics.Color.parseColor(
                    "#bdec3a"
                )
            ),
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Center)
        )
    }

}