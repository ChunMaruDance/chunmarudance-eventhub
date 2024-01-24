package com.chunmaru.eventhub.screens.default_elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultTopBar(
    title: String,
    onBackClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(0),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Spacer(modifier = Modifier.size(10.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Gray.copy(alpha = 0.2f)
                ),
                modifier = Modifier
                    .padding(end = 10.dp)
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
                        .size(45.dp)
                        .padding(5.dp)
                )

            }

            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Light,
                color = Color(
                    android.graphics.Color.parseColor(
                        "#bdec3a"
                    )
                ),
                modifier = Modifier.weight(1f)
            )


        }


    }


}