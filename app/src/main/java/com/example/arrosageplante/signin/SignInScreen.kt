package com.example.arrosageplante.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier
){

    Scaffold { paddingValue ->
        SignInContent(
            modifier = modifier.padding(paddingValue)
            )
    }

}

@Composable
fun SignInContent(
    modifier: Modifier = Modifier
) {

    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmationPassword by remember { mutableStateOf("")}

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = mail,
            onValueChange = { newText ->
                mail = newText
            },
            label = { Text("Email") },
            modifier = modifier.fillMaxWidth(),
        )

        TextField(
            value = password,
            onValueChange = { newText ->
                password = newText
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = modifier.fillMaxWidth()
        )

        TextField(
            value = password,
            onValueChange = { newText ->
                password = newText
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = modifier.fillMaxWidth()
        )

        TextField(
            value = confirmationPassword,
            onValueChange = { newText ->
                confirmationPassword = newText
            },
            label = { Text("Confirm password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = modifier.fillMaxWidth()
        )

        if(confirmationPassword != password && confirmationPassword.isNotBlank()){
            Snackbar(
                modifier = Modifier.padding(16.dp)
            ){
                Text("Les mots de passe ne sont pas identiques")
            }

        }

        }
    }
