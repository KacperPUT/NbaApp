package com.example.nbaappproject.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaappproject.data.TeamRepository
import com.example.nbaappproject.data.model.Game
import com.example.nbaappproject.data.model.toGame
import com.example.nbaappproject.data.response.GameDetailsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import android.util.Log
import com.example.nbaappproject.data.response.PlayerStatsItem
import com.example.nbaappproject.data.viewmodel.Result

sealed class Result<out T> { // Upewnij się, że masz tę klasę w tym pakiecie
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

class GameViewModel(private val repository: TeamRepository = TeamRepository()) : ViewModel() {

    private val _games = MutableStateFlow<Result<List<Game>>>(Result.Loading)
    val games: StateFlow<Result<List<Game>>> = _games

    private val _gameDetails = MutableStateFlow<Result<GameDetailsItem?>>(Result.Loading)
    val gameDetails: StateFlow<Result<GameDetailsItem?>> = _gameDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(false) // Możesz usunąć, jeśli używasz Result
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _playerStats = MutableStateFlow<Result<List<PlayerStatsItem>>>(Result.Loading)
    val playerStats: StateFlow<Result<List<PlayerStatsItem>>> = _playerStats.asStateFlow()

    fun fetchGamesByDate(date: String) {
        viewModelScope.launch {
            _games.update { Result.Loading }
            try {
                val response = repository.getGamesByDate(date)
                if (response.isSuccessful) {
                    val apiGames = response.body()?.games ?: emptyList()
                    _games.update { Result.Success(apiGames.map { it.toGame() }) }
                } else {
                    Log.e("GameViewModel", "API error: ${response.code()}")
                    _games.update { Result.Error(Exception("Błąd API: ${response.code()}")) }
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Network error", e)
                _games.update { Result.Error(e) }
            } finally {
                // _isLoading.value = false // Niepotrzebne przy Result
            }
        }
    }

    fun fetchGameDetails(gameId: Int) {
        viewModelScope.launch {
            _gameDetails.update { Result.Loading }
            try {
                val response = repository.getGameDetails(gameId)
                if (response.isSuccessful) {
                    val gameDetailsItem = response.body()?.response?.firstOrNull()
                    _gameDetails.update { Result.Success(gameDetailsItem) }
                } else {
                    Log.e("GameViewModel", "API error fetching game details: ${response.code()}")
                    _gameDetails.update { Result.Error(Exception("Błąd API: ${response.code()}")) }
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Network error fetching game details", e)
                _gameDetails.update { Result.Error(e) }
            }
        }
    }

    fun fetchPlayerStats(gameId: Int) {
        viewModelScope.launch {
            _playerStats.value = Result.Loading
            try {
                val response = repository.getPlayerStats(gameId)
                _playerStats.value = Result.Success(response.playerStats)
            } catch (e: Exception) {
                _playerStats.value = Result.Error(e)
            }
        }
    }
}