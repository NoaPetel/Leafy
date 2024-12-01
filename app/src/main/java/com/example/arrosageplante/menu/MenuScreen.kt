package com.example.arrosageplante.menu

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arrosageplante.data.DeviceData
import com.example.arrosageplante.data.fetchDeviceData
import com.example.arrosageplante.data.updatePumpStatus
import com.example.arrosageplante.utils.ConnectionButton
import com.example.arrosageplante.utils.MenuTopAppBar
import com.google.android.material.resources.MaterialAttributes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Connection

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
    onNavigateToNewDevice: () -> Unit,
    onNavigateToWatering: () -> Unit
) {
    Scaffold(
        topBar = { MenuTopAppBar(openDrawer = openDrawer) },
        floatingActionButton = { ConnectionButton(onNavigateToNewDevice)}
    ) { paddingValue ->
        MenuContent(
            modifier = modifier.padding(paddingValue),
            onNavigateToWatering = onNavigateToWatering)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuContent(
    modifier: Modifier = Modifier,
    onNavigateToWatering: () -> Unit
) {
    val deviceData = remember { mutableStateOf(DeviceData()) }

    fetchDeviceData { data ->
        Log.d("Menu", "$data")
        deviceData.value = data
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        SectionTitle(text = "Données")

        DataCardsRow(
            firstCardContent = {
                DataCardContent(
                    title = "Humidité",
                    value = "${deviceData.value.humidity} %",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            secondCardContent = {
                DataCardContent(
                    title = "Température ",
                    value = "${deviceData.value.temperature} °C",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DataCardsRow(
            firstCardContent = {
                DataCardContent(
                    title = "Humidité du sol",
                    value = "${deviceData.value.soilMoisture} %",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            secondCardContent = {
                DataCardContent(
                    title = "Niveau d'eau",
                    value = "${deviceData.value.waterlvl} %",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        )

        Spacer(modifier = Modifier.height(64.dp)) // Add space between sections

        SectionTitle(text = "Action")

        ActionCardsRow(
            firstCardContent = {
                ActionCardContent(
                    text = "Arroser la plante",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            },
            secondCardContent = {
                ActionCardContent(
                    text = "Régler une fréquence d'arrosage",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            firstCardAction = { updatePumpStatus(true) },
            secondCardAction = onNavigateToWatering
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp,
            lineHeight = 14.sp
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun DataCardsRow(
    firstCardContent: @Composable () -> Unit,
    secondCardContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .padding(8.dp)
            ) {
                firstCardContent()
            }
        }

        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .padding(8.dp)
            ) {
                secondCardContent()
            }
        }
    }
}

@Composable
fun ActionCardsRow(
    firstCardContent: @Composable () -> Unit,
    secondCardContent: @Composable () -> Unit,
    firstCardAction: () -> Unit,
    secondCardAction: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = firstCardAction)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .padding(8.dp)
            ) {
                firstCardContent()
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = secondCardAction)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .padding(8.dp)
            ) {
                secondCardContent()
            }
        }
    }
}

@Composable
fun DataCardContent(
    title: String,
    value: String? = null,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = contentColor
        )
        value?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineMedium,
                color = contentColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ActionCard(
    text: String,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    action: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = action),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .padding(8.dp)
        ) {
            content()
        }
    }
}

@Composable
fun ActionCardContent(
    text: String,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = contentColor,
        textAlign = TextAlign.Center
    )
}

