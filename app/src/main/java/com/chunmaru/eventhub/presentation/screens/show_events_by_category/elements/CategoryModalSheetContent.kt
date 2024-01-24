package com.chunmaru.eventhub.presentation.screens.show_events_by_category.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chunmaru.eventhub.R

@Composable

fun CategoryModalSheetContent(
    onShowClick: () -> Unit,
    onLikeClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onShowClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.search_icon),
            contentDescription = "show Event",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "show event",
            color = Color.White,
            fontSize = 16.sp
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onLikeClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Delete from favorite",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "like it",
            color = Color.White,
            fontSize = 16.sp
        )
    }


}