package com.example.nbaappproject.data

import com.example.nbaappproject.data.response.GamesResponse
import com.example.nbaappproject.data.response.TeamsResponse
import com.example.nbaappproject.data.response.TeamStatsResponse
import com.example.nbaappproject.data.response.GameDetailsResponse
import com.example.nbaappproject.data.response.PlayerStatsResponse
import com.example.nbaappproject.data.response.PlayersResponse
import com.example.nbaappproject.data.response.GameStatisticsResponse
import retrofit2.Response

class TeamRepository(private val api: NbaApiService = RetrofitInstance.api) {

    suspend fun getTeamStats(teamId: Int, season: String): Response<TeamStatsResponse> = api.getTeamStatistics(teamId, season)

    suspend fun getGamesByDate(date: String): Response<GamesResponse> = api.getGamesByDate(date)

    suspend fun getTeams(): Response<TeamsResponse> = api.getTeams()

    suspend fun getGameDetails(gameId: Int): Response<GameDetailsResponse> = api.getGameDetails(gameId)

    suspend fun getPlayers(teamId: Int, season: String): Response<PlayersResponse> = api.getPlayers(teamId, season)
    suspend fun getAllPlayers(season: String): Response<PlayersResponse> = api.getAllPlayers(season)
    suspend fun getStandings(season: String) = api.getStandings(season)

    suspend fun getPlayerSeasonStats(playerId: Int, season: String): Response<PlayerStatsResponse> = api.getPlayerSeasonStats(season, playerId)

    suspend fun getGameTeamStatistics(gameId: Int): Response<GameStatisticsResponse> = api.getGameTeamStatistics(gameId)

    suspend fun getTeamNames(): Map<Int, String> {
        val response = api.getTeams()
        return if (response.isSuccessful) {
            response.body()?.teams?.filter { it.name != null }?.associate { it.id to it.name!! } ?: emptyMap()
        } else {
            emptyMap()
        }
    }

    suspend fun getPlayerStats(gameId: Int): PlayerStatsResponse {
        return api.getPlayerStats(gameId)
    }
}
