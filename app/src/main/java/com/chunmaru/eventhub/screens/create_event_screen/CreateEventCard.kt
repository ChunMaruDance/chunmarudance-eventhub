package com.chunmaru.eventhub.screens.create_event_screen


import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.R

@Composable
fun BoxScope.CreateEventCard(
    imageUri: String,
    onChange: (String) -> Unit
) {

    val reg =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    onChange(uri.toString())
                }
            }
        }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Gray.copy(0.1f)
            )
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(android.graphics.Color.parseColor("#131313")).copy(0.6f)
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        Pair(0.8f, Color.Transparent),
                        Pair(1f, Color(android.graphics.Color.parseColor("#131313")))
                    )
                )
            )
    )


    if (imageUri.length < 2) {

        Icon(
            modifier = Modifier
                .padding(top = 55.dp)
                .size(80.dp)
                .align(Alignment.Center)
                .clickable {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    reg.launch(intent)
                },
            painter = painterResource(id = R.drawable.add_image),
            contentDescription = "Add image icon",
            tint = Color(
                android.graphics.Color.parseColor(
                    "#bdec3a"
                ),
            )
        )

    } else {
        androidx.compose.material.Card(
            backgroundColor = Color.Transparent,
            shape = RoundedCornerShape(5),
            modifier = Modifier
                .padding(top = 55.dp)
                .align(Alignment.Center)
                .padding(bottom = 10.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    reg.launch(intent)
                }
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Event Image",
                modifier = Modifier.size(240.dp),
                contentScale = ContentScale.FillBounds,
            )
        }

    }


}