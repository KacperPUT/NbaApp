package com.example.nbaappproject.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaappproject.data.TeamRepository
import com.example.nbaappproject.data.model.PlayerUi
import com.example.nbaappproject.data.model.toUi
import com.example.nbaappproject.data.response.PlayerStatsResponse
import com.example.nbaappproject.data.response.TeamStatsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {

    private val repository = TeamRepository()

    private val _teamStats = MutableStateFlow<TeamStatsResponse?>(null)
    val teamStats: StateFlow<TeamStatsResponse?> = _teamStats

    private val _players = MutableStateFlow<List<PlayerUi>>(emptyList())
    val players: StateFlow<List<PlayerUi>> = _players

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _playerSeasonStats = MutableStateFlow<PlayerStatsResponse?>(null)
    val playerSeasonStats: StateFlow<PlayerStatsResponse?> = _playerSeasonStats.asStateFlow()

    fun loadTeamStats(teamId: Int, season: String = "2021") { // Domyślny sezon może zostać
        viewModelScope.launch {
            try {
                val response = repository.getTeamStats(teamId, season)
                if (response.isSuccessful) {
                    _teamStats.value = response.body()
                } else {
                    Log.e("TeamViewModel", "Błąd w getTeamStatistics: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TeamViewModel", "Exception w getTeamStatistics: ${e.localizedMessage}")
            }
        }
    }

    fun loadPlayers(teamId: Int, season: String = "2021") {
        println("Wywołano loadPlayers dla teamId: $teamId z sezonem: $season")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("Przed wywołaniem API getPlayers")
                val response = repository.getPlayers(teamId, season)
                Log.d("TeamViewModel", "Po wywołaniu API getPlayers - obiekt Response: $response")
                Log.d("TeamViewModel", "Czy odpowiedź getPlayers jest udana: ${response.isSuccessful}")
                Log.d("TeamViewModel", "Kod odpowiedzi getPlayers: ${response.code()}")
                println("Odpowiedź getPlayers (body): ${response.body()}")

                if (response.isSuccessful) {
                    val playersApi = response.body()?.players ?: emptyList()
                    println("DEBUG TeamViewModel: Liczba graczy z API: ${playersApi.size}")
                    if (playersApi.isNotEmpty()) {
                        println("DEBUG TeamViewModel: Typ pierwszego gracza z API: ${playersApi.first()::class.java.simpleName}")
                    }

                    val teamsMap = repository.getTeamNames()
                    _players.value = playersApi.map { player ->
                        val teamName = teamsMap[teamId] ?: "Unknown"
                        player.toUi(teamName = teamName)
                    }
                    println("DEBUG TeamViewModel: _players.value size po mapowaniu: ${_players.value.size}")
                    if (_players.value.isNotEmpty()) {
                        println("DEBUG TeamViewModel: Typ pierwszego gracza w _players.value po mapowaniu: ${_players.value.first()::class.java.simpleName}")
                    }
                } else {
                    Log.e("TeamViewModel", "Błąd w getPlayers: ${response.errorBody()?.string()}")
                    _players.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("TeamViewModel", "Exception w loadPlayers: ${e.localizedMessage}")
                _players.value = emptyList()
            } finally {
                _isLoading.value = false
                println("Blok finally w loadPlayers został wykonany")
            }
        }
    }

    fun loadPlayerSeasonStats(playerId: Int, season: String = "2021") { // Możesz dostosować domyślny sezon
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getPlayerSeasonStats(playerId, season)
                if (response.isSuccessful) {
                    _playerSeasonStats.value = response.body()
                } else {
                    Log.e("TeamViewModel", "Błąd w getPlayerSeasonStats: ${response.errorBody()?.string()}")
                    _playerSeasonStats.value = null
                }
            } catch (e: Exception) {
                Log.e("TeamViewModel", "Exception w getPlayerSeasonStats: ${e.localizedMessage}")
                _playerSeasonStats.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
