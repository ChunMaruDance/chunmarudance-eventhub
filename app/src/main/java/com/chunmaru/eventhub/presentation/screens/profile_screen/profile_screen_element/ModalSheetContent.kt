package com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element

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
fun ModalSheetContent(
    onEditProfileClick: () -> Unit,
    onCreateEventClick: () -> Unit,
    onShowFavoriteClick: () -> Unit,
    onAdminPanelClick: () -> Unit,
    onSignOutClick: () -> Unit,
    showAdminPanel: Boolean
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onEditProfileClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.edit_img),
            contentDescription = "render profile",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Edit Profile",
            color = Color.White,
            fontSize = 16.sp
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onCreateEventClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.create_event_img),
            contentDescription = "Add Event",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Add Event",
            color = Color.White,
            fontSize = 16.sp
        )
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onCreateEventClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.statistic),
            contentDescription = "Statistics",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Statistic",
            color = Color.White,
            fontSize = 16.sp
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onShowFavoriteClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Favorite",
            color = Color.White,
            fontSize = 16.sp
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable { onSignOutClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.exit),
            contentDescription = "Exit Author",
            Modifier.size(22.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Sign out",
            color = Color.White,
            fontSize = 16.sp
        )
    }
    if (showAdminPanel) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clickable { onAdminPanelClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.admins),
                contentDescription = "admin panel",
                Modifier.size(22.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Admin panel",
                color = Color.White,
                fontSize = 16.sp
            )
        }

    }


}

