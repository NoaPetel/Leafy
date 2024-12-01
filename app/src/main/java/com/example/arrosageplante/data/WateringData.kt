package com.example.arrosageplante.data

data class WateringData (
    val id: Int = 0,
    val frequencyInDays: Int,
    val lastWateringDate: Long // Timestamp in milliseconds
)

