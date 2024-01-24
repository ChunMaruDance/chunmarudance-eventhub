package com.chunmaru.eventhub.presentation.screens.signIn_screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.chunmaru.eventhub.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class CheckSignInFireBase(
    private val context: Context
) {
    private fun getContext(): Context = context

    private val auth: FirebaseAuth
        get() = Firebase.auth


    fun signInWithGoogle(launcher: ActivityResultLauncher<Intent>) {
        val signInClient = getClient()
        signInClient.signOut()
        launcher.launch(signInClient.signInIntent)
    }

    fun showErrorToast() {
        Toast.makeText(
            getContext(),
            "Ви не авторизувалися", Toast.LENGTH_SHORT
        ).show()
    }

    fun showSuccessToast() {
        Toast.makeText(
            getContext(),
            "Авторизація пройшла успішно", Toast.LENGTH_SHORT
        ).show()
    }
    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)

    }


    fun firebaseSignInWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {

            if (it.isSuccessful) {
                onSuccess()
            } else {
                onError()
            }

        }
    }

}