package com.example.nbaappproject.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaappproject.data.RetrofitInstance
import com.example.nbaappproject.data.model.Player
import com.example.nbaappproject.data.response.PlayersResponse
import com.example.nbaappproject.data.response.TeamStatsItem
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {

    private val _teamStats = MutableStateFlow<TeamStatsItem?>(null) // Zmieniono typ na TeamStatsItem?
    val teamStats: StateFlow<TeamStatsItem?> = _teamStats

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadTeamStats(teamId: Int, season: String = "2023") {
        viewModelScope.launch {
            _isLoading.value = true // Ustawiamy loading na true przed pobraniem danych
            try {
                val response = RetrofitInstance.api.getTeamStatistics(teamId, season)
                if (response.isSuccessful) {
                    _teamStats.value = response.body()?.stats?.firstOrNull()
                } else {
                    println("Błąd w getTeamStatistics: ${response.errorBody()}")
                    _teamStats.value = null // W przypadku błędu ustawiamy na null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _teamStats.value = null // W przypadku wyjątku ustawiamy na null
            } finally {
                _isLoading.value = false // Zawsze ustawiamy loading na false po próbie pobrania danych
            }
        }
    }

    fun loadPlayers(teamId: Int, season: String = "2023") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.api.getPlayers(teamId, season)
                println("Kod odpowiedzi getPlayers: ${response.code()}") // Dodaj ten log
                if (response.isSuccessful) {
                    println("Odpowiedź getPlayers (body): ${response.body()}")
                    val playersResponse = response.body()?.players ?: emptyList()
                    _players.value = playersResponse
                } else {
                    println("Błąd w getPlayers: ${response.errorBody()}")
                    _players.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _players.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}