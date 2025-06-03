package com.example.nbaappproject.data

import com.example.nbaappproject.data.model.*
import com.example.nbaappproject.data.response.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NbaApiService {

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("teams")
    suspend fun getTeams(): Response<TeamsResponse>

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("players")
    suspend fun getPlayers(
        @Query("team") teamId: Int,
        @Query("season") season: String
    ): Response<PlayersResponse>

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("players")
    suspend fun getAllPlayers(
        @Query("season") season: String
    ): Response<PlayersResponse>

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("teams/statistics")
    suspend fun getTeamStatistics(
        @Query("id") teamId: Int,
        @Query("season") season: String
    ): Response<TeamStatsResponse>

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("standings")
    suspend fun getStandings(
        @Query("season") season: String,
        @Query("league") league: String = "standard" // Domyślna wartość
    ): Response<StandingsResponse>

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("games")
    suspend fun getGamesByDate(
        @Query("date") date: String
    ): Response<GamesResponse>

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("games")
    suspend fun getGameDetails(
        @Query("id") gameId: Int
    ): Response<GameDetailsResponse>

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("players/statistics")
    suspend fun getPlayerStats( // Ta funkcja jest używana do pobierania statystyk gracza z konkretnego meczu
        @Query("game") gameId: Int
    ): PlayerStatsResponse

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("players/statistics") // Ten endpoint jest używany do statystyk sezonowych gracza
    suspend fun getPlayerSeasonStats( // Ta funkcja jest używana do pobierania statystyk gracza z danego sezonu
        @Query("season") season: String,
        @Query("id") playerId: Int
    ): Response<PlayerStatsResponse> // Zwraca PlayerStatsResponse, które zawiera listę PlayerStatsItem

    @Headers(
        "X-RapidAPI-Key: c555ec5e0bmshf7d9602845d710dp1e61d9jsn74a3dd4c6d60",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("games/statistics")
    suspend fun getGameTeamStatistics(
        @Query("id") gameId: Int
    ): Response<GameStatisticsResponse>
}
