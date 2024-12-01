package com.example.arrosageplante.watering

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrosageplante.signin.SignInContent
import com.example.arrosageplante.viewmodel.WateringViewModel

@Composable
fun WateringScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit,
    wateringViewModel: WateringViewModel
) {
    Scaffold { paddingValue ->
        WateringContent (
            modifier = modifier.padding(paddingValue),
            onNavigateToMenu = onNavigateToMenu,
            wateringViewModel
        )
    }
}

@Composable
fun WateringContent(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit,
    viewModel: WateringViewModel
) {
    var selectedDays by remember { mutableStateOf(7f) }
    var showSnackbar by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Choose Watering Frequency",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Water every ${selectedDays.toInt()} day(s)",
            style = MaterialTheme.typography.bodyMedium
        )

        Slider(
            value = selectedDays,
            onValueChange = { selectedDays = it },
            valueRange = 1f..30f,
            steps = 29,
            modifier = Modifier.fillMaxWidth()
        )


        Button(
            onClick = {
                viewModel.setWateringFrequency(selectedDays.toInt(), true)
                showSnackbar = true
                onNavigateToMenu()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Save")
        }
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onNavigateToMenu()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Return to Menu")
        }
    }

    if (showSnackbar) {
        Snackbar {
            Text("Watering frequency saved")
        }
    }
}