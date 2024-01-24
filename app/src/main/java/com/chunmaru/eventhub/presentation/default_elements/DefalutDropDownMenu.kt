package com.chunmaru.eventhub.presentation.default_elements

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chunmaru.eventhub.data.model.optional.MenuItem

@Composable
fun DefaultDropDownMenuItems(
    items: List<MenuItem>,
    onItemCheckChange: (String) -> Unit
) {

    items.forEach { item ->
        DropdownMenuItem(onClick = {
            onItemCheckChange(item.title)
        }) {

            Switch(
                checked = item.isSelect,
                onCheckedChange = {
                    onItemCheckChange(item.title)
                },
                thumbContent = if (item.isSelect) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                        )
                    }
                } else null,
                colors = SwitchDefaults.colors(
                    checkedIconColor = Color(android.graphics.Color.parseColor("#bdec3a")),
                    checkedThumbColor = Color(android.graphics.Color.parseColor("#1A1A1A")),
                    checkedTrackColor = MaterialTheme.colorScheme.background,
                    uncheckedIconColor = Color(android.graphics.Color.parseColor("#6a8b0e")),
                    uncheckedThumbColor = Color(android.graphics.Color.parseColor("#404040")),
                    uncheckedTrackColor = Color(android.graphics.Color.parseColor("#0d0d0d")),
                    uncheckedBorderColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = item.title,
                color = Color.White,
                fontSize = 16.sp
            )


        }
    }


}