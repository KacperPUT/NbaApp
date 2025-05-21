package com.example.nbaappproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaappproject.data.RetrofitInstance
import com.example.nbaappproject.data.response.Standing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StandingsViewModel : ViewModel() {

    private val _standings = MutableStateFlow<List<Standing>>(emptyList())
    val standings: StateFlow<List<Standing>> = _standings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadStandings(season: String = "2023") {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = RetrofitInstance.api.getStandings(season)
                if (response.isSuccessful) {
                    _standings.value = response.body()?.standings ?: emptyList()
                } else {
                    _errorMessage.value = "Błąd API: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Wystąpił wyjątek: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
