package com.chunmaru.eventhub.presentation.screens.change_profile_screen.change_profile_elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chunmaru.eventhub.data.model.author.Author

@Composable
fun ChangeProfileScreenContent(
    author: Author,
    description: String,
    userName: String,
    onAvatarChangeClick: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(20.dp))

        AsyncImage(
            model = author.avatar.uri,
            contentDescription = "User Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(175.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    onAvatarChangeClick()
                }
        )

        Spacer(modifier = Modifier.size(90.dp))
        ChangeProfileCard(
            userName = userName,
            userDescription = description,
            onAvatarChangeClick = onAvatarChangeClick,
            onDescriptionChange = onDescriptionChange,
            onUserNameChange = onUserNameChange
        )


    }


}