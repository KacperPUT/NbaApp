package com.example.nbaappproject.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaappproject.data.RetrofitInstance
import com.example.nbaappproject.data.model.Player
import com.example.nbaappproject.ui.theme.Teams.TeamStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {

    private val _teamStats = MutableStateFlow<TeamStats?>(null)
    val teamStats: StateFlow<TeamStats?> = _teamStats

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadTeamStats(teamId: Int, season: String = "2023") {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTeamStatistics(teamId, season)
                if (response.isSuccessful) {
                    _teamStats.value = response.body()?.stats?.firstOrNull()
                } else {
                    println("Błąd w getTeamStatistics: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadPlayers(teamId: Int, season: String = "2023") {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPlayers(teamId, season)
                if (response.isSuccessful) {
                    _players.value = response.body()?.players ?: emptyList()
                } else {
                    println("Błąd w getPlayers: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
