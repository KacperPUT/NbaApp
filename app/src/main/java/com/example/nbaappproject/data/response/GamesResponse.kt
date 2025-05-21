package com.example.nbaappproject.data.response

import com.google.gson.annotations.SerializedName

data class GamesResponse(
    @SerializedName("response") val games: List<GameItem>
)

data class GameItem(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: GameDate,
    @SerializedName("teams") val teams: GameTeams,
    @SerializedName("scores") val scores: GameScores
)

data class GameDate(
    @SerializedName("start") val start: String
)

data class GameTeams(
    @SerializedName("home") val home: GameTeam,
    @SerializedName("visitors") val visitors: GameTeam
)

data class GameTeam(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String,
    @SerializedName("logo") val logo: String
)

data class GameScores(
    @SerializedName("home") val home: GameScore,
    @SerializedName("visitors") val visitors: GameScore
)

data class GameScore(
    @SerializedName("points") val points: Int
)
