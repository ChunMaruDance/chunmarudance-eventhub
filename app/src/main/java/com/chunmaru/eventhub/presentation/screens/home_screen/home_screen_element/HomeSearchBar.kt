package com.chunmaru.eventhub.presentation.screens.home_screen.home_screen_element


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chunmaru.eventhub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar() {

    val text = remember {
        mutableStateOf("")
    }

    TextField(
        value = text.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        onValueChange = {
            text.value = it
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Gray.copy(alpha = 0.2f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search_icon),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(25.dp),
                    tint = Color(android.graphics.Color.parseColor("#B8B8B8"))
                )
        },
        placeholder = {
            Text(
                text = "Write event name",
                color = Color(android.graphics.Color.parseColor("#B8B8B8"))
            )
        },
        singleLine = true,
        maxLines = 1,
        shape = RoundedCornerShape(50)
    )


}