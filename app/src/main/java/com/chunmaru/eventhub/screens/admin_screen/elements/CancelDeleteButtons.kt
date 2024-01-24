package com.chunmaru.eventhub.screens.admin_screen.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable

fun CancelDeleteButtons(
    cancelClick: () -> Unit,
    deleteClick: () -> Unit,
) {

    Row(
        modifier = Modifier.padding(start = 10.dp, top = 2.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray.copy(alpha = 0.2f)

            ),
            modifier = Modifier
                .padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(20))
                .clickable {
                    cancelClick()
                },
            shape = RoundedCornerShape(20),

            ) {

            Text(
                text = "Cancel",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(
                    android.graphics.Color.parseColor(
                        "#bdec3a"
                    )
                ),
                modifier = Modifier.padding(10.dp)
            )

        }


        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray.copy(alpha = 0.2f)
            ),
            modifier = Modifier
                .padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                .clip(RoundedCornerShape(20))
                .clickable {
                    deleteClick()
                },
            shape = RoundedCornerShape(20),

            ) {

            Text(
                text = "Delete",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(
                    android.graphics.Color.parseColor(
                        "#bdec3a"
                    )
                ), //
                modifier = Modifier.padding(10.dp)
            )

        }

    }

}