package com.chunmaru.eventhub.presentation.screens.change_profile_screen.change_profile_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chunmaru.eventhub.R
import com.chunmaru.eventhub.presentation.screens.create_event_screen.elements.CustomMultilineHintTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeProfileCard(
    userName: String,
    userDescription: String,
    onAvatarChangeClick: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onAvatarChangeClick()
                }
        ) {

            Text(text = "Profile Pictures", color = Color.White)
            Text(text = "JPEG,PNG and less than 10MB", color = Color.Gray)
        }

        Icon(
            painter = painterResource(id = R.drawable.edit_img),
            contentDescription = "Edit icon",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )


    }
    Spacer(modifier = Modifier.size(30.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
    ) {
        Text(
            text = "ABOUT", color = Color.Gray,
            fontSize = 16.sp,
        )
        Text(
            text = "Username",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, top = 5.dp)
        )
        BasicTextField(
            value = userName,
            onValueChange = {
                if (it.length <= 36)
                    onUserNameChange(it)
            },
            textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.background(Color.Transparent),
                ) {
                    if (userName.isEmpty()) {
                        Text(
                            text = "Write your event name",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier.offset(y = (-10).dp)
        )
        Text(
            text = "Description",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )

        val descriptionLimit = remember {
            mutableIntStateOf(userDescription.length)
        }

        CustomMultilineHintTextField(
            value = userDescription,
            onValueChanged = {
                descriptionLimit.intValue = it.length
                if (it.length < 2000) onDescriptionChange(it)
            },
            hint = "Write your description",
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5))
                .background(Color(android.graphics.Color.parseColor("#1A1A1A")))
                .padding(5.dp),
            maxLines = 6,
            minLines = 4
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "${descriptionLimit.intValue}/2000",
                color = Color.Gray,
                fontSize = 12.sp,
            )
        }

        Spacer(modifier = Modifier.size(70.dp))


    }


}