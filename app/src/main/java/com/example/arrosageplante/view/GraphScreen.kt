package com.example.arrosageplante.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.arrosageplante.data.DeviceDataPoint
import com.example.arrosageplante.data.fetchDeviceDataFromFirestore
import com.example.arrosageplante.utils.ConnectionButton
import com.example.arrosageplante.utils.MenuTopAppBar
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


@Composable
fun GraphScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit
){
    Scaffold(
        topBar = { MenuTopAppBar(openDrawer = onNavigateToMenu) },
    ) { paddingValue ->
        GraphContent(
            modifier = modifier.padding(paddingValue),
        )
    }
}

@Composable
fun GraphContent(
    modifier: Modifier = Modifier,
) {
    val dataPoints = remember { mutableStateOf<List<DeviceDataPoint>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Fetch data when the composable is first created
    LaunchedEffect(Unit) {
        try {
            dataPoints.value = fetchDeviceDataFromFirestore()
            isLoading.value = false
        } catch (e: Exception) {
            errorMessage.value = "Failed to fetch device data: ${e.message}"
            isLoading.value = false
        }
    }

    // Main content
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        else if (errorMessage.value != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage.value ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        else if (dataPoints.value.isNotEmpty()) {
            // Title
            Text(
                text = "Device Data Graphs",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    GraphCard(
                        title = "Temperature",
                        entries = convertToLineEntries(dataPoints.value) { it.temperature },
                        color = Color.Red
                    )
                }
                item {
                    GraphCard(
                        title = "Humidity",
                        entries = convertToLineEntries(dataPoints.value) { it.humidity },
                        color = Color.Blue
                    )
                }
                item {
                    GraphCard(
                        title = "Water Level",
                        entries = convertToLineEntries(dataPoints.value) { it.waterlvl },
                        color = Color.Green
                    )
                }
                item {
                    GraphCard(
                        title = "Soil Moisture",
                        entries = convertToLineEntries(dataPoints.value) { it.soilmoisture },
                        color = Color.Cyan
                    )
                }
            }
        }

        else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No device data available",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Composable
fun GraphCard(
    title: String,
    entries: List<Entry>,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                factory = { context ->
                    LineChart(context).apply {
                        description.isEnabled = false
                        setNoDataText("No data available")
                        setTouchEnabled(true)
                        isDragEnabled = true
                        setScaleEnabled(true)
                        setPinchZoom(true)
                        

                        axisLeft.textColor = Color.White.toArgb()
                        axisRight.textColor = Color.White.toArgb()
                        xAxis.textColor = Color.White.toArgb()


                    }
                },
                update = { chart ->
                    val dataSet = LineDataSet(entries, title).apply {
                        this.color = color.toArgb()
                        valueTextColor = Color.White.toArgb()
                        lineWidth = 2f
                        setCircleColor(color.toArgb())
                        setDrawCircleHole(false)
                        setDrawValues(false)

                        chart.legend.textColor = Color.White.toArgb()
                    }

                    val lineData = LineData(dataSet)
                    chart.data = lineData
                    chart.invalidate()
                }
            )
        }
    }
}

fun convertToLineEntries(
    dataPoints: List<DeviceDataPoint>,
    valueSelector: (DeviceDataPoint) -> Double
): List<Entry> {
    return dataPoints.mapIndexed { index, dataPoint ->
        Entry(index.toFloat(), valueSelector(dataPoint).toFloat())
    }
}