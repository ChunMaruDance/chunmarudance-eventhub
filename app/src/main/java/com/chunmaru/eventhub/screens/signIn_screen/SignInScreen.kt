package com.chunmaru.eventhub.screens.signIn_screen

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit
) {

    val viewModel: SignInViewModel = hiltViewModel()

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    viewModel.checkSignInFireBase.firebaseSignInWithGoogle(account.idToken!!,
                        onSuccess = {
                            viewModel.createAuthor()
                            onSignInSuccess()

                        },
                        onError = {
                            Log.d("MyTag", "SignInScreen: error ")
                        }
                    )
                }

            } catch (e: ApiException) {
                Log.d("MyTag", "SignInScreen: $e ")
            }

        }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier)

            Column {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .padding(end = 10.dp, top = 5.dp, bottom = 5.dp)
                        .clip(RoundedCornerShape(20))
                        .clickable {
                            viewModel.checkSignInFireBase.signInWithGoogle(launcher)
                        },
                    shape = RoundedCornerShape(20),

                    ) {
                    androidx.compose.material3.Text(
                        text = "Get Started",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(
                            android.graphics.Color.parseColor(
                                "#bdec3a"
                            )
                        ),
                        modifier = Modifier.padding(15.dp)
                    )

                }


            }


        }


    }


}