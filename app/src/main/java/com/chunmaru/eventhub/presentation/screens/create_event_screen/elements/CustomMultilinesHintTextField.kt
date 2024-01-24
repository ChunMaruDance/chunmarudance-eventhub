package com.chunmaru.eventhub.presentation.screens.create_event_screen.elements


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun CustomMultilineHintTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier,
    hint: String = "",
    textStyle: TextStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
    maxLines: Int = 5,
    minLines: Int = 3,
) {

    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle = textStyle,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = { innerTextField ->
            Box(
                modifier = modifier,
            ) {
                if (value.isEmpty()) {
                    Text(text = hint, color = Color.Gray, fontSize = 16.sp)
                }
                innerTextField()
            }
        },

        )


}