package com.chunmaru.eventhub.presentation.navigation

import android.net.Uri

import com.chunmaru.eventhub.data.model.author.Author
import com.chunmaru.eventhub.data.model.event.Event
import com.chunmaru.eventhub.data.model.ImageData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson


sealed class Screen(
    val route: String
) {

    object HomeRoute : Screen(
        HOME_ROUTE
    )

    object SignInScreen : Screen(
        SING_IN
    )

    object EventsScreen : Screen(
        EVENTS
    )

    object ProfileScreen : Screen(
        PROFILE
    )

    object FavoriteScreen : Screen(
        FAVORITE
    )

    object SearchScreen : Screen(
        SEARCH
    )

    object ProfileRoute : Screen(
        PROFILE_ROUTE
    )

    object AdminScreen : Screen(
        ADMIN
    )


    object ChangeProfileScreen : Screen(
        CHANGE_PROFILE
    ) {

        private const val ROUTE_FOR_ARGS = "change_profile"
        fun getRouteWithArgs(author: Author): String {
            val authorJson = Gson().toJson(author)
            return "$ROUTE_FOR_ARGS/${authorJson.encode()}"
        }
    }

    object CreateEventScreen : Screen(
        CREATE_EVENT
    ) {
        private const val ROUTE_FOR_ARGS = "create_event"

        fun getRouteWithArgs(event: Event?): String {
            val eventToSerialize = event ?: Event(
                id = "",
                name = "",
                description = "",
                date = "pick date",
                time = "pick time",
                imgUri = ImageData("", ""),
                categories = listOf(),
                authorId = Firebase.auth.uid!!,
                city = "Kyiv",
            )
            val eventJson = Gson().toJson(eventToSerialize)
            return "$ROUTE_FOR_ARGS/${eventJson.encode()}"


        }

    }

    object ShowEventsByCategoryScreen : Screen(
        SHOW_EVENTS_CATEGORY
    ) {
        private const val ROUTE_FOR_ARGS = "show_events_category"

        fun getRouteWithArgs(category: String): String {
            val categoryJson = Gson().toJson(category)
            return "$ROUTE_FOR_ARGS/${categoryJson.encode()}"
        }

    }


    object EventInfoScreen : Screen(
        EVENT_INFO
    ) {

        private const val ROUTE_FOR_ARGS = "event_info"

        fun getRouteWithArgs(event: Event): String {
            val eventJson = Gson().toJson(event)
            return "$ROUTE_FOR_ARGS/${eventJson.encode()}"
        }

    }

    object CommentsHomeScreen : Screen(
        COMMENTS_HOME
    ) {
        private const val ROUTE_FOR_ARGS = "comments_home"

        fun getRouteWithArgs(event: Event): String {
            val eventJson = Gson().toJson(event)
            return "$ROUTE_FOR_ARGS/${eventJson.encode()}"
        }
    }


    object CommentsScreen : Screen(
        COMMENTS
    ) {
        private const val ROUTE_FOR_ARGS = "comments"

        fun getRouteWithArgs(event: Event): String {
            val eventJson = Gson().toJson(event)
            return "$ROUTE_FOR_ARGS/${eventJson.encode()}"
        }

    }


    companion object {
        const val KEY_EVENT = "key_event"
        const val KEY_CREATE_EVENT = "key_create_event"
        const val KEY_PROFILE = "key_profile"
        const val KEY_CATEGORY = "key_category"
        const val KEY_COMMENTS = "key_comments"
        const val KEY_COMMENTS_HOME = "key_comments_home"

        //
        const val COMMENTS_HOME = "comments_home/{$KEY_COMMENTS_HOME}"
        const val COMMENTS = "comments/{$KEY_COMMENTS}"
        const val CHANGE_PROFILE = "change_profile/{$KEY_PROFILE}"
        const val SEARCH = "search"
        const val PROFILE = "profile"
        const val EVENTS = "events"
        const val FAVORITE = "favorite"
        const val ADMIN = "admin"
        const val SHOW_EVENTS_CATEGORY = "show_events_category/{$KEY_CATEGORY}"
        const val CREATE_EVENT = "create_event/{$KEY_CREATE_EVENT}"
        const val HOME_ROUTE = "home route"
        const val PROFILE_ROUTE = "profile route"
        const val EVENT_INFO = "event_info/{$KEY_EVENT}"
        const val SING_IN = "sign in"

    }

    fun String.encode(): String {
        return Uri.encode(this)
    }
}