package com.example.celebrare_assignment

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    state: SignInState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onGoogleSignInClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var isLoginScreen by remember { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF289F61)) {
        Box(contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.celebrare),
                        contentDescription = "Celebrare Logo",
                        modifier = Modifier
                            .size(150.dp)
                            .padding(bottom = 10.dp)
                    )

                    AnimatedContent(
                        targetState = isLoginScreen,
                        label = "AuthScreenAnimation"
                    ) { isLogin ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (isLogin) {
                                LoginContent(state, onEmailChange, onPasswordChange, onLoginClick)
                            } else {
                                SignUpContent(state, onEmailChange, onPasswordChange, onSignUpClick)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f))
                        Text("OR", modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)
                        Divider(modifier = Modifier.weight(1f))
                    }

                    IconButton(onClick = onGoogleSignInClick, modifier = Modifier.size(46.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Sign in with Google",
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = { isLoginScreen = !isLoginScreen }) {
                        Text(
                            text = if (isLoginScreen) "Need an account? SIGN UP" else "Already a user? LOGIN",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginContent(
    state: SignInState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    OutlinedTextField(
        value = state.email,
        onValueChange = onEmailChange,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = onLoginClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF289F61))
    ) {
        Text("LOGIN")
    }
}

@Composable
private fun SignUpContent(
    state: SignInState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit
) {
    OutlinedTextField(
        value = state.email,
        onValueChange = onEmailChange,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = onSignUpClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF289F61))
    ) {
        Text("SIGN UP")
    }
}