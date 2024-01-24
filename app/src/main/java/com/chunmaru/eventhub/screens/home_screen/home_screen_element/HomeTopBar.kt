package com.chunmaru.eventhub.screens.home_screen.home_screen_element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chunmaru.eventhub.R

@Composable
fun HomeTopBar() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            //   .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp)
            .padding(start = 12.dp, end = 12.dp, top = 7.dp)
            .clip(RoundedCornerShape(10))
            .background(
                Color(android.graphics.Color.parseColor("#262626")).copy(
                    alpha = 0.4f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "EventHub", fontSize = 26.sp,
            color = Color(
                android.graphics.Color.parseColor(
                    "#bdec3a"
                )
            ),
            modifier = Modifier.padding(10.dp)
        )
    }

}