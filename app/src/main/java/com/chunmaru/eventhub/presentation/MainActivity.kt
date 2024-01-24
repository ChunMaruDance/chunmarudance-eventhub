package com.chunmaru.eventhub.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.chunmaru.eventhub.presentation.screens.MainScreen
import com.chunmaru.eventhub.presentation.screens.signIn_screen.SignInScreen
import com.chunmaru.eventhub.presentation.ui.theme.EventHubTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            if (Firebase.auth.currentUser == null) {
                SignInScreen(
                    onSignInSuccess = {
                        recreate()
                    }
                )
            } else Content()

        }
    }

    @Composable
    private fun Content() {
        EventHubTheme {
                MainScreen()
        }
    }

}


