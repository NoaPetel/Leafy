package com.example.arrosageplante.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewDeviceScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DeviceNameTextField()

            Text(
                text = "Not finished"
            )
        }
    }
}

@Composable
fun DeviceNameTextField(
    modifier: Modifier = Modifier
) {
    var deviceName by rememberSaveable { mutableStateOf("") }

    TextField(
        value = deviceName,
        onValueChange = { deviceName = it },
        modifier = modifier.fillMaxWidth(),
        label = { Text("Device Name") },
        placeholder = { Text("Enter device name") },
        singleLine = true
    )
}