package com.example.celebrare_assignment

import android.content.IntentSender

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val email: String = "",
    val password: String = ""
)


data class SignInResult(
    val data: UserData?,
    val errorMessage: String?,
    val intentSender: IntentSender? = null
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)