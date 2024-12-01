package com.example.arrosageplante.utils

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

@Composable
fun TemperatureChart(temperatureList: List<Double>) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            LineChart(context).apply {
                val entries = temperatureList.mapIndexed { index, temp ->
                    Entry(index.toFloat(), temp.toFloat())
                }

                val dataSet = LineDataSet(entries, "Températures")
                dataSet.setColor(0xFF6200EE.toInt()) // Couleur de la courbe
                dataSet.setDrawValues(false) // Ne pas afficher les valeurs sur le graphique

                val lineData = LineData(dataSet)
                this.data = lineData
                this.invalidate() // Rafraîchir le graphique
            }
        }
    )
}

