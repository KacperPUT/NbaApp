package com.example.nbaappproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaappproject.data.RetrofitInstance
import com.example.nbaappproject.data.model.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeamsViewModel : ViewModel() {

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadTeams()
    }

    fun loadTeams() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = RetrofitInstance.api.getTeams()
                if (response.isSuccessful) {
                    _teams.value = response.body()?.teams ?: emptyList()
                } else {
                    _errorMessage.value = "Błąd API: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Błąd: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
