package com.example.arrosageplante.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.example.arrosageplante.ui.theme.TextColor
import com.example.arrosageplante.ui.theme.primaryDarkColor

@Composable
fun ConnectionButton(
    onNavigateToNewDevice: () -> Unit
){

    FloatingActionButton(
        onClick =
            onNavigateToNewDevice,
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add New Device"
            )

        },
        containerColor = primaryDarkColor,
        contentColor = TextColor
    )
}