package com.chunmaru.eventhub.screens.profile_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun ModalProfileSheetContent(
    onCommentClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onCommentClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.comment),
            contentDescription = "comment event",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Comments",
            color = Color.White,
            fontSize = 16.sp
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onDeleteClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.delete),
            contentDescription = "delete event",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Delete",
            color = Color.White,
            fontSize = 16.sp
        )
    }


}