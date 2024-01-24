package com.chunmaru.eventhub.screens.default_elements


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DefaultProgressBar() {

    Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.TopCenter
    ) {

        CircularProgressIndicator(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(40.dp),
            color = Color(android.graphics.Color.parseColor("#bdec3a"))
        )
    }

}