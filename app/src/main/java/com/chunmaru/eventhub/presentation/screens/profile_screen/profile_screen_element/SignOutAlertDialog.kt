package com.chunmaru.eventhub.presentation.screens.profile_screen.profile_screen_element

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SignOutAlertDialog(
    onSignOutConfirmed: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        title = { Text("Sign Out") },
        text = { Text("Are you sure you want to sign out?") },
        confirmButton = {
            Button(
                onClick = {
                    onSignOutConfirmed.invoke()
                    onDismiss.invoke()
                }
            ) {
                Text("Sign Out")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss.invoke() }) {
                Text("Cancel")
            }
        }
    )
}