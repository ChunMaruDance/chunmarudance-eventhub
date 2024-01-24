package com.chunmaru.eventhub.screens.home_screen.home_screen_element


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chunmaru.eventhub.data.model.EventType

@Composable
fun HomeTabBar(
    types: List<EventType>,
    onCategoriesClick: (String) -> Unit
) {
    val expandedState = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Category", fontSize = 26.sp,
            color = Color(
                android.graphics.Color.parseColor(
                    "#bdec3a"
                )
            ),
            modifier = Modifier.padding(start = 5.dp)

        )

        val rotationState = animateFloatAsState(
            targetValue =
            if (expandedState.value) 180f
            else 0f, label = ""
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(50)),
            shape = RoundedCornerShape(35),

            ) {
            Row(
                modifier = Modifier
                    .width(45.dp)
                    .height(45.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .rotate(rotationState.value)
                        .clickable {
                            expandedState.value = !expandedState.value
                        },
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "More categories",
                    tint = Color(
                        android.graphics.Color.parseColor(
                            "#bdec3a"
                        )
                    )

                )
            }

        }

    }

    if (expandedState.value) {
        Card(
            modifier = Modifier
                .padding(start = 5.dp, end = 15.dp)
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        delayMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            colors = CardDefaults.cardColors(
                containerColor = Color(android.graphics.Color.parseColor("#1a1a1a"))
            ),
        ) {

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.Center,
            ) {
                items(items = types) { type ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (type.selected) Color.Gray.copy(alpha = 0.2f)
                            else Color.Transparent
                        ),
                        modifier = Modifier
                            .padding(end = 7.dp, top = 3.dp, bottom = 3.dp, start = 5.dp)
                            .clip(RoundedCornerShape(30))
                            .clickable {
                                onCategoriesClick(type.type)
                            },
                        shape = RoundedCornerShape(35),

                        ) {

                        Text(
                            text = type.type,
                            fontSize = 20.sp,
                            fontWeight = if (type.selected) FontWeight.Bold
                            else FontWeight.Normal,
                            color = if (type.selected) Color(
                                android.graphics.Color.parseColor(
                                    "#bdec3a"  // Color.Gray.copy(alpha = 0.2f)
                                )
                            ) //
                            else Color(
                                android.graphics.Color.parseColor(
                                    "#B8B8B8"
                                )
                            ),
                            modifier = Modifier.padding(10.dp)
                        )

                    }
                }

            }


        }

    }


}




