package com.chunmaru.eventhub.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.chunmaru.eventhub.domain.navigation.BottomNavItem
import com.chunmaru.eventhub.domain.navigation.AppNavGraph
import com.chunmaru.eventhub.domain.navigation.Screen
import com.chunmaru.eventhub.domain.navigation.rememberNavigationState
import com.chunmaru.eventhub.presentation.screens.admin_screen.AdminScreen
import com.chunmaru.eventhub.presentation.screens.change_profile_screen.ChangeProfileScreen
import com.chunmaru.eventhub.presentation.screens.comments_screen.CommentScreen
import com.chunmaru.eventhub.presentation.screens.create_event_screen.CreateEventScreen
import com.chunmaru.eventhub.presentation.screens.favorite_screen.FavoriteScreen
import com.chunmaru.eventhub.presentation.screens.home_screen.HomeScreen
import com.chunmaru.eventhub.presentation.screens.home_screen.home_screen_element.HomeBottomBar
import com.chunmaru.eventhub.presentation.screens.profile_screen.ProfileScreen
import com.chunmaru.eventhub.presentation.screens.search_screen.SearchScreen
import com.chunmaru.eventhub.presentation.screens.show_event_screen.ShowEventScreen
import com.chunmaru.eventhub.presentation.screens.show_events_by_category.ShowEventsByCategoryScreen
import com.chunmaru.eventhub.presentation.screens.signIn_screen.SignInScreen

@Composable
fun MainScreen() {

    val items = BottomNavItem.DEFAULT
    val navigationState = rememberNavigationState()
    val bottomBarVisible = remember { mutableStateOf(true) }
    Scaffold(
        bottomBar = {
            if (bottomBarVisible.value) {
                HomeBottomBar(
                    items = items,
                    navigationState = navigationState,
                    onItemClick = {
                        navigationState.navigateTo(it.screen.route)
                    }

                )
            }


        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            AppNavGraph(
                navHostController = navigationState.navHostController,
                eventsScreenCall = {

                    HomeScreen(
                        onEventClick = { event ->
                            navigationState.navigateToInfo(event)
                        },
                        onAllEventClick = { category ->
                            navigationState.navigateToCategory(category)
                        })


                },
                eventInfoCall = {
                    ShowEventScreen(it,
                        onBackClick = {
                            navigationState.navHostController.popBackStack()
                        },
                        onCommentClick = { event ->
                            navigationState.navigationToCommentsHome(event)
                        })
                },
                favoriteCall = {
                    FavoriteScreen(
                        onEventClick = { event ->
                            navigationState.navigateToInfoFromFavorite(event)
                        }
                    )
                },
                profileCall = {
                    ProfileScreen(
                        onChangeProfileClick = { author ->
                            navigationState.navigationToProfile(author)
                        },
                        onCreateEventClick = {
                            navigationState.navigateToCreateEvent(null)
                        },
                        onShowFavoriteClick = {
                            navigationState.navigateTo(Screen.FAVORITE)
                        },
                        onSignInClick = {
                            navigationState.navigateToSignInScreen()
                        },
                        onEventClick = {
                            navigationState.navigateToCreateEvent(it)
                        },
                        onAdminClick = {
                            navigationState.navigateToAdmin()
                        }

                    )
                },
                searchCall = {
                    SearchScreen(
                        onEventClick = { event ->
                            navigationState.navigateToInfoFromFavorite(event)
                        },
                        onAuthorClick = { author ->
                            //todo
                        }
                    )
                },
                changeProfileCall = {
                    ChangeProfileScreen(it,
                        onBackClick = {
                            navigationState.navHostController.popBackStack()
                        },
                        onSaveData = {
                            navigationState.navHostController.popBackStack()
                        }
                    )
                },
                createEventCall = {
                    CreateEventScreen(
                        onBackClick = {
                            navigationState.navHostController.popBackStack()
                        },
                        onCommentsClick = { event ->
                            navigationState.navigationToComments(event)
                        },
                        it
                    )
                },
                signInCall = {
                    bottomBarVisible.value = false
                    SignInScreen(onSignInSuccess = {
                        navigationState.navHostController.navigate(Screen.HOME_ROUTE)
                        bottomBarVisible.value = true
                    })
                },
                showEventsCategoryCall = { category ->
                    ShowEventsByCategoryScreen(category,
                        onEventClick = { event ->
                            navigationState.navigateToInfoFromFavorite(event)
                        },
                        onBackClick = {
                            navigationState.navHostController.popBackStack()
                        })


                },
                onAdminCall = {
                    AdminScreen(
                        onEventClick = { event ->
                            navigationState.navigateToInfoFromFavorite(event)
                        },
                        onBackClick = {
                            navigationState.navHostController.popBackStack()
                        }
                    )
                },
                onCommentsCall = { event ->
                    CommentScreen(event = event,
                        onBackClick = {
                            navigationState.navHostController.popBackStack()
                        })

                },
                onCommentsHomeCall = { event ->
                    CommentScreen(event = event,
                        onBackClick = {
                            navigationState.navHostController.popBackStack()
                        })
                }
            )

        }

    }
}


