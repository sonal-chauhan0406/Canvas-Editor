package com.example.celebrare_assignment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val authRepository by lazy {
        AuthRepository(
            oneTapClient = Identity.getSignInClient(applicationContext),
            googleAuthUiClient = googleAuthUiClient
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val signInViewModel = viewModel<SignInViewModel>()
            val state by signInViewModel.state.collectAsStateWithLifecycle()
            LaunchedEffect(key1 = Unit) {
                if (authRepository.getSignedInUser() != null) {
                    signInViewModel.onSignInResult(
                        SignInResult(data = authRepository.getSignedInUser(), errorMessage = null)
                    )
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult = authRepository.signInWithGoogleIntent(
                                intent = result.data ?: return@launch
                            )
                            signInViewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            if (state.isSignInSuccessful) {
                CanvasPagerScreen(
                    onSignOutClick = {
                        lifecycleScope.launch {
                            authRepository.signOut()
                            Toast.makeText(applicationContext, "Signed Out", Toast.LENGTH_SHORT).show()
                            signInViewModel.resetState()
                        }
                    }
                )
            } else {
                AuthScreen(
                    state = state,
                    onEmailChange = signInViewModel::onEmailChange,
                    onPasswordChange = signInViewModel::onPasswordChange,
                    onGoogleSignInClick = {
                        lifecycleScope.launch {
                            val result = authRepository.signInWithGoogle()
                            result.intentSender?.let { sender ->
                                launcher.launch(IntentSenderRequest.Builder(sender).build())
                            }
                            result.errorMessage?.let { error ->
                                signInViewModel.onSignInResult(result)
                            }
                        }
                    },
                    onLoginClick = {
                        lifecycleScope.launch {
                            val result = authRepository.signInWithEmail(state.email, state.password)
                            signInViewModel.onSignInResult(result)
                        }
                    },
                    onSignUpClick = {
                        lifecycleScope.launch {
                            val result = authRepository.signUpWithEmail(state.email, state.password)
                            signInViewModel.onSignInResult(result)
                        }
                    }
                )
            }

            state.signInError?.let { error ->
                Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                signInViewModel.onSignInResult(SignInResult(data = null, errorMessage = null))
            }
        }
    }
}