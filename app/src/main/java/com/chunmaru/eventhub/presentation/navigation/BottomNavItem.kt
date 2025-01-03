package com.chunmaru.eventhub.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val screen: Screen,
    val title: String,
    val imageVector: ImageVector
) {

    object Home : BottomNavItem(
        screen = Screen.HomeRoute,
        title = "Home",
        imageVector = Icons.Outlined.Home
    )

    object Favorite : BottomNavItem(
        screen = Screen.FavoriteScreen,
        title = "Favorite",
        imageVector = Icons.Outlined.FavoriteBorder
    )

    object Profile : BottomNavItem(
        screen = Screen.ProfileRoute,
        title = "Profile",
        imageVector = Icons.Outlined.Person
    )

    object Search : BottomNavItem(
        screen = Screen.SearchScreen,
        title = "Search",
        imageVector = Icons.Outlined.Search
    )

    companion object {
        val DEFAULT = listOf(
            Home,
            Search,
            Favorite,
            Profile
        )
    }


}