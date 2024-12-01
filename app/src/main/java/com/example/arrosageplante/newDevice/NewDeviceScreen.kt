package com.example.arrosageplante.newDevice

import android.view.Menu
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NewDeviceTaskScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit,
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { paddingValue ->
        Text(
            modifier = modifier.fillMaxSize(),
            text = "Hello"
        )

    }
}


