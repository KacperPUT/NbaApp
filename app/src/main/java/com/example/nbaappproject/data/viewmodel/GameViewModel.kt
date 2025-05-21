package com.example.nbaappproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaappproject.data.TeamRepository
import com.example.nbaappproject.data.model.Game
import com.example.nbaappproject.data.model.toGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class GameViewModel(private val repository: TeamRepository = TeamRepository()) : ViewModel() {

    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> = _games
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchGamesByDate(date: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getGamesByDate(date)
                if (response.isSuccessful) {
                    val apiGames = response.body()?.games ?: emptyList()
                    _games.value = apiGames.map { it.toGame() }
                } else {
                    Log.e("GameViewModel", "API error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("GameViewModel", "Network error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}