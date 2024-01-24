package com.chunmaru.eventhub.screens.profile_screen.profile_screen_element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chunmaru.eventhub.R

@Composable
fun ProfileScreenTopBar(
    userName: String,
    onAddEventClick: () -> Unit,
    onMoreClick: () -> Unit,

    ) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(0),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Row {
                IconButton(onClick = {
                    onAddEventClick()
                }) {

                    Icon(
                        painter = painterResource(id = R.drawable.create_event_img),
                        contentDescription = "Create Event",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                }

                IconButton(onClick = {
                    onMoreClick()
                }) {

                    Icon(
                        painter = painterResource(id = R.drawable.more_img),
                        contentDescription = "Create Event",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )


                }


            }


        }


    }

}