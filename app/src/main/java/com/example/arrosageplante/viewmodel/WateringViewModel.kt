package com.example.arrosageplante.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrosageplante.data.saveWateringFrequency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WateringViewModel : ViewModel() {

    private val _wateringFrequency = MutableStateFlow(0)
    val wateringFrequency: StateFlow<Int> = _wateringFrequency.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadWateringFrequency()
    }

    private fun loadWateringFrequency() {
        viewModelScope.launch {
            _isLoading.value = true
            _wateringFrequency.value = fetchWateringFrequencyFromDataSource()
            _isLoading.value = false
        }
    }

    fun setWateringFrequency(frequency: Int, enable: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            saveWateringFrequencyToDataSource(frequency, enable)
            _wateringFrequency.value = frequency
            _isLoading.value = false
        }
    }

    private suspend fun fetchWateringFrequencyFromDataSource(): Int {
        return 0
    }

    private suspend fun saveWateringFrequencyToDataSource(frequency: Int, enable: Boolean) {
        saveWateringFrequency(frequency, enable)
    }
}