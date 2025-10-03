package com.example.celebrare_assignment

import android.content.Intent
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class AuthRepository(
    private val oneTapClient: SignInClient,
    private val googleAuthUiClient: GoogleAuthUiClient
) {
    private val auth = Firebase.auth

    fun getSignedInUser(): UserData? = googleAuthUiClient.getSignedInUser()

    suspend fun signInWithGoogleIntent(intent: Intent): SignInResult {
        return googleAuthUiClient.signInWithIntent(intent)
    }

    suspend fun signInWithGoogle(): SignInResult {
        val signInIntentSender = googleAuthUiClient.signIn()
        return SignInResult(data = null, errorMessage = null, intentSender = signInIntentSender)
    }

    suspend fun signInWithEmail(email: String, pass: String): SignInResult {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            SignInResult(data = getSignedInUser(), errorMessage = null)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(data = null, errorMessage = e.message)
        }
    }

    suspend fun signUpWithEmail(email: String, pass: String): SignInResult {
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
            SignInResult(data = getSignedInUser(), errorMessage = null)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(data = null, errorMessage = e.message)
        }
    }

    suspend fun signOut() {
        googleAuthUiClient.signOut()
    }
}
