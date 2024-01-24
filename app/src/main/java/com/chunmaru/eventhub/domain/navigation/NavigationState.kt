package com.chunmaru.eventhub.domain.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.Event

class NavigationState(
    val navHostController: NavHostController
) {
    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

    }

    fun navigateToSignInScreen() {
        navHostController.navigate(Screen.SignInScreen.route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToInfo(event: Event) {
        navHostController.navigate(Screen.EventInfoScreen.getRouteWithArgs(event)) {
//            anim {
//                enter = com.google.android.material.R.anim.abc_slide_in_bottom
//                exit = com.google.android.material.R.anim.abc_slide_out_bottom
//            }
        }
    }

    fun navigateToCategory(category: String) {
        navHostController.navigate(Screen.ShowEventsByCategoryScreen.getRouteWithArgs(category))
    }


    fun navigateToInfoFromFavorite(event: Event) {
        navHostController.navigate(Screen.EventInfoScreen.getRouteWithArgs(event)) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigationToProfile(author: Author) {
        navHostController.navigate(Screen.ChangeProfileScreen.getRouteWithArgs(author))
    }

    fun navigationToComments(event: Event) {
        navHostController.navigate(Screen.CommentsScreen.getRouteWithArgs(event))
    }

    fun navigationToCommentsHome(event: Event) {
        navHostController.navigate(Screen.CommentsHomeScreen.getRouteWithArgs(event))
    }


    fun navigateInGraph(route: String) {
        navHostController.navigate(route)
    }

    fun navigateToCreateEvent(event: Event?) {
        navHostController.navigate(Screen.CreateEventScreen.getRouteWithArgs(event))
    }

    fun navigateToAdmin() {
        navHostController.navigate(Screen.AdminScreen.route)
    }

}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController = navHostController)
    }

}