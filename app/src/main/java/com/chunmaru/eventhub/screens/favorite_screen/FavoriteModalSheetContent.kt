package com.chunmaru.eventhub.screens.favorite_screen

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
fun FavoriteModalSheetContent(
    onShowClick: () -> Unit,
    onDeleteFromFavoriteClick: () -> Unit,
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
            .clickable { onDeleteFromFavoriteClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.delete),
            contentDescription = "Delete from favorite ",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "delete from favorite",
            color = Color.White,
            fontSize = 16.sp
        )
    }


}