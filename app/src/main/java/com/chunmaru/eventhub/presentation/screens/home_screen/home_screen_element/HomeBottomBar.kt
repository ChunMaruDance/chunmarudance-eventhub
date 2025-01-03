package com.chunmaru.eventhub.presentation.screens.home_screen.home_screen_element

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.chunmaru.eventhub.presentation.navigation.BottomNavItem
import com.chunmaru.eventhub.presentation.navigation.NavigationState

@Composable
fun HomeBottomBar(
    items: List<BottomNavItem>,
    navigationState: NavigationState,
    onItemClick: (BottomNavItem) -> Unit,
) {

    NavigationBar(
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(15.dp)
    ) {
        Card(
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(
                containerColor = Color(android.graphics.Color.parseColor("#262626")).copy(alpha = 0.4f)
            ),//#262626
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Transparent),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                items.forEachIndexed { index, item ->

                    val navBackStackEntry =
                        navigationState.navHostController.currentBackStackEntryAsState()

                    val selected = navBackStackEntry.value?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(26.dp)
                                .clickable {
                                    onItemClick(item)
                                },
                            imageVector = item.imageVector,
                            contentDescription = null,
                            tint = if (selected == true) Color(android.graphics.Color.parseColor("#bdec3a"))
                            else Color(android.graphics.Color.parseColor("#808080"))
                        )

                        if (selected != null) {
                            AnimatedVisibility(
                                visible = selected,
                                enter = slideInHorizontally() + fadeIn(),
                                exit = slideOutHorizontally() + fadeOut()
                            ) {
                                Box(
                                    Modifier
                                        .width(6.dp)
                                        .height(2.dp)
                                        .background(Color(android.graphics.Color.parseColor("#bdec3a")))
                                )
                            }

                        }

                    }

                }

            }

        }

    }

}


