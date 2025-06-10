package com.example.nbaappproject.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaappproject.data.TeamRepository
import com.example.nbaappproject.data.model.PlayerUi
import com.example.nbaappproject.data.model.toUi
import com.example.nbaappproject.data.response.PlayerStatsResponse
import com.example.nbaappproject.data.response.PlayerStatsItem
import com.example.nbaappproject.data.response.TeamStatsResponse
import com.example.nbaappproject.data.response.GameStatisticsResponse
import com.example.nbaappproject.data.model.PlayerSeasonAverages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {

    private val repository = TeamRepository()

    private val _teamStats = MutableStateFlow<TeamStatsResponse?>(null)
    val teamStats: StateFlow<TeamStatsResponse?> = _teamStats.asStateFlow()

    private val _players = MutableStateFlow<List<PlayerUi>>(emptyList())
    val players: StateFlow<List<PlayerUi>> = _players.asStateFlow()

    private val _playerSeasonStats = MutableStateFlow<PlayerStatsResponse?>(null)
    val playerSeasonStats: StateFlow<PlayerStatsResponse?> = _playerSeasonStats.asStateFlow()

    private val _playerSeasonAverages = MutableStateFlow<PlayerSeasonAverages?>(null)
    val playerSeasonAverages: StateFlow<PlayerSeasonAverages?> = _playerSeasonAverages.asStateFlow()

    private val _gameTeamStatistics = MutableStateFlow<GameStatisticsResponse?>(null)
    val gameTeamStatistics: StateFlow<GameStatisticsResponse?> = _gameTeamStatistics.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadTeamStats(teamId: Int, season: String = "2023") {
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

    fun loadPlayerSeasonStats(playerId: Int, season: String = "2021") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPlayerSeasonStats(playerId, season)
                if (response.isSuccessful) {
                    val playerStatsList = response.body()?.playerStats ?: emptyList()
                    _playerSeasonStats.value = response.body()

                    if (playerStatsList.isNotEmpty()) {
                        val totalGames = playerStatsList.size
                        var totalPoints = 0
                        var totalAssists = 0
                        var totalTotalRebounds = 0
                        var totalOffensiveRebounds = 0
                        var totalDefensiveRebounds = 0
                        var totalSteals = 0
                        var totalBlocks = 0
                        var totalPersonalFouls = 0
                        var totalTurnovers = 0
                        var totalPlusMinus = 0
                        var totalFieldGoalsMade = 0
                        var totalFieldGoalsAttempted = 0
                        var totalFreeThrowsMade = 0
                        var totalFreeThrowsAttempted = 0
                        var totalThreePointsMade = 0
                        var totalThreePointsAttempted = 0
                        var totalMinutesInSeconds = 0

                        playerStatsList.forEach { stat: PlayerStatsItem ->
                            totalPoints += stat.points ?: 0
                            totalAssists += stat.assists ?: 0
                            totalTotalRebounds += stat.totalRebounds ?: 0
                            totalOffensiveRebounds += stat.offensiveRebounds ?: 0
                            totalDefensiveRebounds += stat.defensiveRebounds ?: 0
                            totalSteals += stat.steals ?: 0
                            totalBlocks += stat.blocks ?: 0
                            totalPersonalFouls += stat.personalFouls ?: 0
                            totalTurnovers += stat.turnovers ?: 0
                            stat.plusMinus?.toIntOrNull()?.let { totalPlusMinus += it }

                            stat.minutes?.let { minString ->
                                val parts = minString.split(":")
                                if (parts.size == 2) {
                                    try {
                                        val minutes = parts[0].toInt()
                                        val seconds = parts[1].toInt()
                                        totalMinutesInSeconds += (minutes * 60) + seconds
                                    } catch (e: NumberFormatException) {
                                        Log.e("TeamViewModel", "Błąd parsowania minut: $minString")
                                    }
                                }
                            }
                            totalFieldGoalsMade += stat.fieldGoalsMade ?: 0
                            totalFieldGoalsAttempted += stat.fieldGoalsAttempted ?: 0
                            totalFreeThrowsMade += stat.freeThrowsMade ?: 0
                            totalFreeThrowsAttempted += stat.freeThrowsAttempted ?: 0
                            totalThreePointsMade += stat.threePointsMade ?: 0
                            totalThreePointsAttempted += stat.threePointsAttempted ?: 0
                        }

                        val avgPoints = if (totalGames > 0) totalPoints.toDouble() / totalGames else 0.0
                        val avgAssists = if (totalGames > 0) totalAssists.toDouble() / totalGames else 0.0
                        val avgTotalRebounds = if (totalGames > 0) totalTotalRebounds.toDouble() / totalGames else 0.0
                        val avgOffensiveRebounds = if (totalGames > 0) totalOffensiveRebounds.toDouble() / totalGames else 0.0
                        val avgDefensiveRebounds = if (totalGames > 0) totalDefensiveRebounds.toDouble() / totalGames else 0.0
                        val avgSteals = if (totalGames > 0) totalSteals.toDouble() / totalGames else 0.0
                        val avgBlocks = if (totalGames > 0) totalBlocks.toDouble() / totalGames else 0.0
                        val avgPersonalFouls = if (totalGames > 0) totalPersonalFouls.toDouble() / totalGames else 0.0
                        val avgTurnovers = if (totalGames > 0) totalTurnovers.toDouble() / totalGames else 0.0
                        val avgPlusMinus = if (totalGames > 0) totalPlusMinus.toDouble() / totalGames else 0.0

                        val avgFieldGoalPercentage = if (totalFieldGoalsAttempted > 0) (totalFieldGoalsMade.toDouble() / totalFieldGoalsAttempted) * 100 else 0.0
                        val avgFreeThrowPercentage = if (totalFreeThrowsAttempted > 0) (totalFreeThrowsMade.toDouble() / totalFreeThrowsAttempted) * 100 else 0.0
                        val avgThreePointPercentage = if (totalThreePointsAttempted > 0) (totalThreePointsMade.toDouble() / totalThreePointsAttempted) * 100 else 0.0

                        val avgMinutesFormatted = if (totalGames > 0) {
                            val averageSeconds = totalMinutesInSeconds / totalGames
                            val avgMin = averageSeconds / 60
                            val avgSec = averageSeconds % 60
                            String.format("%02d:%02d", avgMin, avgSec)
                        } else "00:00"

                        _playerSeasonAverages.value = PlayerSeasonAverages(
                            playerId = playerId,
                            firstName = playerStatsList.first().player.firstname,
                            lastName = playerStatsList.first().player.lastname,
                            teamName = playerStatsList.first().team.name,
                            gamesPlayed = totalGames,
                            avgPoints = String.format("%.1f", avgPoints).toDouble(),
                            avgAssists = String.format("%.1f", avgAssists).toDouble(),
                            avgTotalRebounds = String.format("%.1f", avgTotalRebounds).toDouble(),
                            avgOffensiveRebounds = String.format("%.1f", avgOffensiveRebounds).toDouble(),
                            avgDefensiveRebounds = String.format("%.1f", avgDefensiveRebounds).toDouble(),
                            avgSteals = String.format("%.1f", avgSteals).toDouble(),
                            avgBlocks = String.format("%.1f", avgBlocks).toDouble(),
                            avgPersonalFouls = String.format("%.1f", avgPersonalFouls).toDouble(),
                            avgTurnovers = String.format("%.1f", avgTurnovers).toDouble(),
                            avgPlusMinus = String.format("%.1f", avgPlusMinus).toDouble(),
                            avgMinutes = avgMinutesFormatted,
                            avgFieldGoalPercentage = String.format("%.1f", avgFieldGoalPercentage).toDouble(),
                            avgFreeThrowPercentage = String.format("%.1f", avgFreeThrowPercentage).toDouble(),
                            avgThreePointPercentage = String.format("%.1f", avgThreePointPercentage).toDouble()
                        )
                    } else {
                        _playerSeasonAverages.value = null
                    }

                } else {
                    Log.e("TeamViewModel", "Błąd w getPlayerSeasonStats: ${response.errorBody()?.string()}")
                    _playerSeasonStats.value = null
                    _playerSeasonAverages.value = null
                }
            } catch (e: Exception) {
                Log.e("TeamViewModel", "Exception w getPlayerSeasonStats: ${e.localizedMessage}")
                _playerSeasonStats.value = null
                _playerSeasonAverages.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadGameTeamStatistics(gameId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getGameTeamStatistics(gameId)
                if (response.isSuccessful) {
                    _gameTeamStatistics.value = response.body()
                } else {
                    Log.e("TeamViewModel", "Błąd w loadGameTeamStatistics: ${response.errorBody()?.string()}")
                    _gameTeamStatistics.value = null
                }
            } catch (e: Exception) {
                Log.e("TeamViewModel", "Exception w loadGameTeamStatistics: ${e.localizedMessage}")
                _gameTeamStatistics.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
