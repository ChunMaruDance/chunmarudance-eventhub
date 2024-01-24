package com.chunmaru.eventhub.screens.create_event_screen.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chunmaru.eventhub.R

@Composable
fun CityDropdownMenu(
    cities: List<String>,
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedCity, color = Color.Gray
            )
            IconButton(onClick = {
                expanded = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "Edit icon",
                    tint = Color(android.graphics.Color.parseColor("#bdec3a")),
                    modifier = Modifier.size(40.dp)
                )
            }

        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(color = Color(android.graphics.Color.parseColor("#1A1A1A")))
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    onClick = {
                        onCitySelected(city)
                        expanded = false
                    },
                    modifier = Modifier.background(color = Color(android.graphics.Color.parseColor("#1A1A1A")))
                ) {
                    Text(text = city, color = Color.Gray)
                }
            }
        }
    }
}