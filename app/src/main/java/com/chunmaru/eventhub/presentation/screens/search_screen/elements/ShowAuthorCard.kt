package com.chunmaru.eventhub.presentation.screens.search_screen.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.data.model.author.Author

@Composable
fun ShowAuthorCard(
    author: Author,
    cornerRadius: RoundedCornerShape,
    onClick: (Author) -> Unit
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
            .clickable {
                onClick(author)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(60.dp)) {

            AsyncImage(
                model = author.avatar.uri,
                contentDescription = "Event Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(60.dp)
                    .clip(cornerRadius),
            )
        }

        Spacer(modifier = Modifier.size(10.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = author.name, color = Color.White, fontSize = 16.sp)
            Text(text = "author in app", color = Color.Gray, fontSize = 12.sp)

        }
    }

}