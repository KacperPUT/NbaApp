package com.example.nbaappproject.data

import com.example.nbaappproject.data.response.GamesResponse
import com.example.nbaappproject.data.response.TeamStatsResponse
import retrofit2.Response

class TeamRepository {
    suspend fun getTeamStats(teamId: Int, season: String): Response<TeamStatsResponse> {
        return RetrofitInstance.api.getTeamStatistics(teamId, season)
    }

    // 🆕 Nowa metoda do pobierania meczów na dany dzień
    suspend fun getGamesByDate(date: String): Response<GamesResponse> {
        return RetrofitInstance.api.getGamesByDate(date)
    }
}
