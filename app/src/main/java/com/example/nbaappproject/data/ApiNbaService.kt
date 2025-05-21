package com.example.nbaappproject.data

import com.example.nbaappproject.data.model.*
import com.example.nbaappproject.data.response.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NbaApiService {

    @Headers(
        "X-RapidAPI-Key: YOUR_API_KEY",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("teams")
    suspend fun getTeams(): Response<TeamsResponse>

    @Headers(
        "X-RapidAPI-Key: YOUR_API_KEY",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("players")
    suspend fun getPlayers(
        @Query("team") teamId: Int,
        @Query("season") season: String
    ): Response<PlayersResponse>

    @GET("players")
    suspend fun getAllPlayers(
        @Query("season") season: String
    ): Response<PlayersResponse>

    @Headers(
        "X-RapidAPI-Key: YOUR_API_KEY",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("statistics/teams/statistics")
    suspend fun getTeamStatistics(
        @Query("id") teamId: Int,
        @Query("season") season: String
    ): Response<TeamStatsResponse>

    @Headers(
        "X-RapidAPI-Key: YOUR_API_KEY",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("standings")
    suspend fun getStandings(
        @Query("season") season: String
    ): Response<StandingsResponse>

    @Headers(
        "X-RapidAPI-Key: YOUR_API_KEY",
        "X-RapidAPI-Host: api-nba-v1.p.rapidapi.com"
    )
    @GET("games")
    suspend fun getGamesByDate(
        @Query("date") date: String
    ): Response<GamesResponse>

}