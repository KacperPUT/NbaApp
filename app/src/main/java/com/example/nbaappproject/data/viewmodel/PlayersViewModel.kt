package com.example.nbaappproject.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbaappproject.data.RetrofitInstance
import com.example.nbaappproject.data.TeamsRepository
import com.example.nbaappproject.data.model.PlayerUi
import com.example.nbaappproject.data.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayersViewModel : ViewModel() {

    private val _players = MutableStateFlow<List<PlayerUi>>(emptyList())
    val players: StateFlow<List<PlayerUi>> = _players

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val teamsRepository = TeamsRepository()

    fun loadPlayers(season: String = "2023") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val playersResponse = RetrofitInstance.api.getAllPlayers(season)
                val teams = teamsRepository.getTeamNames()

                if (playersResponse.isSuccessful) {
                    val playersApi = playersResponse.body()?.players ?: emptyList()
                    _players.value = playersApi.map { it.toUi(teamName = teams[it.teamId] ?: "Unknown") }
                } else {
                    Log.e("PlayersVM", "Błąd API: ${playersResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("PlayersVM", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
