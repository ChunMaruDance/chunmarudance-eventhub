package com.chunmaru.eventhub.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.chunmaru.eventhub.data.model.Author
import com.chunmaru.eventhub.data.model.Event
import com.google.gson.Gson

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    eventsScreenCall: @Composable () -> Unit,
    eventInfoCall: @Composable (Event) -> Unit,
    favoriteCall: @Composable () -> Unit,
    profileCall: @Composable () -> Unit,
    searchCall: @Composable () -> Unit,
    changeProfileCall: @Composable (Author) -> Unit,
    createEventCall: @Composable (Event) -> Unit,
    signInCall: @Composable () -> Unit,
    showEventsCategoryCall: @Composable (String) -> Unit,
    onAdminCall: @Composable () -> Unit,
    onCommentsCall: @Composable (Event) -> Unit,
    onCommentsHomeCall: @Composable (Event) -> Unit
) {

    NavHost(
        navController = navHostController,
        startDestination = Screen.HomeRoute.route
    ) {

        navigation(
            startDestination = Screen.EventsScreen.route,
            route = Screen.HomeRoute.route
        ) {
            composable(route = Screen.EventsScreen.route) {
                eventsScreenCall()
            }
            composable(
                route = Screen.EventInfoScreen.route,
                arguments = listOf(
                    navArgument(Screen.KEY_EVENT) {
                        type = NavType.StringType
                    }
                ),
            ) {
                val eventJson = it.arguments?.getString(Screen.KEY_EVENT) ?: ""
                val event = Gson().fromJson(eventJson, Event::class.java)
                eventInfoCall(event)
            }

            composable(route = Screen.ShowEventsByCategoryScreen.route,
                arguments = listOf(
                    navArgument(Screen.KEY_CATEGORY) {
                        type = NavType.StringType
                    }
                )) {
                val categoryJson = it.arguments?.getString(Screen.KEY_CATEGORY) ?: ""
                val category = Gson().fromJson(categoryJson, String::class.java)
                showEventsCategoryCall(category)
            }

            composable(route = Screen.CommentsHomeScreen.route,
                arguments = listOf(
                    navArgument(Screen.KEY_COMMENTS_HOME) {
                        type = NavType.StringType
                    }
                )) {
                val eventJson = it.arguments?.getString(Screen.KEY_COMMENTS_HOME) ?: ""
                val event = Gson().fromJson(eventJson, Event::class.java)
                onCommentsHomeCall(event)
            }


        }
        composable(route = Screen.SING_IN) {
            signInCall()
        }


        composable(route = Screen.FAVORITE) {
            favoriteCall()
        }

        composable(route = Screen.SEARCH) {
            searchCall()
        }
        navigation(
            startDestination = Screen.ProfileScreen.route,
            route = Screen.ProfileRoute.route
        ) {

            composable(Screen.PROFILE) {
                profileCall()
            }

            composable(route = Screen.CHANGE_PROFILE,
                arguments = listOf(
                    navArgument(Screen.KEY_PROFILE) {
                        type = NavType.StringType
                    }
                )) {
                val authorJson = it.arguments?.getString(Screen.KEY_PROFILE) ?: ""
                val author = Gson().fromJson(authorJson, Author::class.java)
                changeProfileCall(author)
            }
            composable(
                Screen.CREATE_EVENT,
                arguments = listOf(
                    navArgument(Screen.KEY_CREATE_EVENT) {
                        type = NavType.StringType
                    })
            ) {
                val eventJson = it.arguments?.getString(Screen.KEY_CREATE_EVENT) ?: ""
                val event = Gson().fromJson(eventJson, Event::class.java)
                createEventCall(event)
            }

            composable(Screen.ADMIN) {
                onAdminCall()
            }

            composable(
                route = Screen.COMMENTS,
                arguments = listOf(
                    navArgument(Screen.KEY_COMMENTS) {
                        type = NavType.StringType
                    })
            ) {
                val eventJson = it.arguments?.getString(Screen.KEY_COMMENTS) ?: ""
                val event = Gson().fromJson(eventJson, Event::class.java)
                onCommentsCall(event)
            }


        }

    }

}