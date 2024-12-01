package com.example.arrosageplante.login

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.arrosageplante.R
import com.example.arrosageplante.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

val auth: FirebaseAuth by lazy {
    FirebaseAuth.getInstance()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateToSignIn: () -> Unit,
    onNavigateToMenu: () -> Unit,
    userViewModel: UserViewModel
){

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ){ paddingValues ->
        LoginContent(
            modifier = modifier.padding(paddingValues),
            onNavigateToSignIn = onNavigateToSignIn,
            onNavigateToMenu = onNavigateToMenu,
            userViewModel = userViewModel,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun LoginContent(
    modifier: Modifier = Modifier,
    onNavigateToSignIn: () -> Unit,
    onNavigateToMenu: () -> Unit,
    userViewModel: UserViewModel,
    snackbarHostState: SnackbarHostState
){
    var mail by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        // horizontalAlignment = Alignment.CenterHorizontally
    ){

        TextField(
            value = mail,
            onValueChange = {newText ->
                mail = newText
            },
            label = { Text("Email") },
            modifier = modifier.fillMaxWidth(),
        )

        TextField(
            value = password,
            onValueChange = {newText ->
                password = newText
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = modifier.fillMaxWidth()
            )

        Button(
            modifier = modifier
                .fillMaxWidth(),
            onClick = {
                signIn(
                    email = mail,
                    password = password,
                    onSuccess = onNavigateToMenu,
                    onFailure = { exception ->
                        val message = when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Identifiants incorrects. Veuillez réessayer."
                            is FirebaseAuthInvalidUserException -> "Aucun compte trouvé avec cet email. Vérifiez votre email ou créez un compte."
                            is FirebaseNetworkException -> "Problème de connexion réseau. Vérifiez votre connexion internet."
                            else -> "La connexion a échoué. Veuillez réessayer."
                        }

                        // Log the error
                        Log.e("LoginFailure", "Échec de connexion", exception)
                        Log.d("LoginFailure", message)

                        // Show Snackbar
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Long
                            )
                        }
                    },
                    userViewModel = userViewModel)
                Log.d("ViewLogin", "Connexion cliqué")
            }
        ) {
            Text(text = "Connexion")
        }

        Text(
            text = "Mot de passe oublié ?",
            modifier = modifier
                .clickable(){
                    Log.d("LoginScreen", "Mot de passe oublié cliqué")
                }
        )


        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp
        )


        Button(
            modifier = modifier
                .fillMaxWidth(),
            onClick = {
                onNavigateToSignIn();
                Log.d("ViewLogin", "Créer compte cliqué")
            }
        ) {
            Text(text = "Créer un compte")
        }
    }
}

fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit, userViewModel: UserViewModel) {

    if (email.isBlank() || password.isBlank()) {
        val emptyCredentialsException = Exception("Email or password cannot be empty")
        Log.d("SignIn", "Empty credentials attempt")
        onFailure(emptyCredentialsException)
        return
    }

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("SignIn", "Connexion réussie")
                userViewModel.setUserData(auth.currentUser)
                onSuccess()
            } else {
                val errorMessage = when (task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
                    is FirebaseAuthInvalidUserException -> "User account does not exist"
                    else -> "Sign-in failed: ${task.exception?.message}"
                }
                Log.e("SignIn", errorMessage)
                onFailure(task.exception ?: Exception("Unknown sign-in error"))
            }


        }
}

@Composable
fun failure(exception: Exception? = null, view: View) {
    // Log the error
    Log.e("LoginFailure", "Échec de connexion", exception)

    // Potential error messages based on common Firebase Auth errors
    val errorMessage = when (exception) {
        is FirebaseAuthInvalidCredentialsException -> "Identifiants incorrects. Veuillez réessayer."
        is FirebaseAuthInvalidUserException -> "Aucun compte trouvé avec cet email. Vérifiez votre email ou créez un compte."
        is FirebaseNetworkException -> "Problème de connexion réseau. Vérifiez votre connexion internet."
        else -> "La connexion a échoué. Veuillez réessayer."
    }

    // Show Snackbar with the error message
    Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG)
        .setBackgroundTint(ContextCompat.getColor(view.context, R.color.error_red))
        .setTextColor(ContextCompat.getColor(view.context, R.color.white))
        .show()

    // Continue logging the error message
    Log.d("LoginFailure", errorMessage)
}

suspend fun showSnackbar(snackbarHostState: SnackbarHostState, message: String) {
    snackbarHostState.showSnackbar(
        message = message,
        actionLabel = "Dismiss", // Optional action label
        duration = SnackbarDuration.Short // Optional duration
    )
}