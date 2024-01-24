package com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.R

@Composable
fun ProfileCard(
    avatarUri: String,
    userName: String,
    userDescription: String,
    onMoreClick: () -> Unit,
    onEditProfile: () -> Unit,
    onAddEvent: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {

            val loader = remember {
                mutableStateOf(true)
            }
            Row {

                Box(
                    Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (loader.value) {
                        CircularProgressIndicator(
                            color = Color(android.graphics.Color.parseColor("#bdec3a")),
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }

                    AsyncImage(
                        model = avatarUri, contentDescription = "User Icon",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(50)),
                        contentScale = ContentScale.Crop,
                        onLoading = {
                            loader.value = true
                        },
                        onSuccess = {
                            loader.value = false
                        }
                    )

                }


                Spacer(modifier = Modifier.size(15.dp))
                Column {
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = userName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userDescription,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                }


            }

        }
        Spacer(modifier = Modifier.size(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {


                IconButton(onClick = {
                    onEditProfile()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit_img),
                        contentDescription = "edit profile",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.size(6.dp))
                IconButton(onClick = {
                    onMoreClick()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "create event",
                        tint = Color.Gray,
                        modifier = Modifier.size(25.dp)
                    )

                }
            }
            Row {
                IconButton(onClick = {
                    onAddEvent()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.create_event_img),
                        contentDescription = "create event",
                        tint = Color.Gray,
                        modifier = Modifier.size(25.dp)
                    )

                }

                IconButton(onClick = {

                }) {

                    Icon(
                        painter = painterResource(id = R.drawable.play_button),
                        contentDescription = "create event",
                        tint = Color(android.graphics.Color.parseColor("#bdec3a")),
                        modifier = Modifier.size(40.dp)
                    )

                }

            }


        }
        Spacer(modifier = Modifier.size(20.dp))


    }


}