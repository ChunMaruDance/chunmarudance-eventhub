package com.chunmaru.eventhub.presentation.screens.comments_screen.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.data.model.reviews.ReviewAuthor

@Composable
fun CommentCard(
    reviewAuthor: ReviewAuthor
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = reviewAuthor.auhtor.avatar.uri,
                contentDescription = "author image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(50))
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                Text(
                    text = reviewAuthor.auhtor.name, color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = reviewAuthor.date, color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.offset(y = (-7).dp)
                )
            }
        }

        Text(
            text = reviewAuthor.review,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 75.dp)
                .offset(y = (-20).dp)
        )


    }


}