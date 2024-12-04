package com.example.arrosageplante.utils

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    entries: List<Entry>,
    label: String
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                // Configure chart appearance
                setNoDataText("No data available")
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
            }
        },
        update = { chart ->
            // Create dataset
            val dataSet = LineDataSet(entries, label).apply {
                color = Color.BLUE
                valueTextColor = Color.WHITE
                lineWidth = 2f
                setCircleColor(Color.BLUE)
                setDrawCircleHole(false)
                setDrawValues(false)
            }

            // Set data to chart
            val lineData = LineData(dataSet)
            chart.data = lineData
            chart.invalidate() // Refresh chart
        }
    )
}

