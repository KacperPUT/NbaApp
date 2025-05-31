package com.example.nbaappproject.data.response

import com.google.gson.annotations.SerializedName

data class GameDetailsResponse(
    @SerializedName("response") val response: List<GameDetailsItem>
)

data class GameDetailsItem(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: GameDate, // Używamy istniejącej GameDate
    @SerializedName("teams") val teams: GameTeams, // Używamy istniejącej GameTeams
    @SerializedName("scores") val scores: GameDetailsGameScores,
    @SerializedName("status") val status: GameStatus,
    @SerializedName("periods") val periods: GamePeriods,
    @SerializedName("arena") val arena: GameArena,
    @SerializedName("officials") val officials: List<String>?,
    @SerializedName("timesTied") val timesTied: Int?,
    @SerializedName("leadChanges") val leadChanges: Int?,
    @SerializedName("nugget") val nugget: String?
)

data class GameDetailsGameScores(
    @SerializedName("visitors") val visitors: TeamScoreDetails,
    @SerializedName("home") val home: TeamScoreDetails
)

data class TeamScoreDetails(
    @SerializedName("points") val points: Int,
    @SerializedName("linescore") val linescore: List<String>?
)

data class GameStatus(
    @SerializedName("clock") val clock: String?,
    @SerializedName("halftime") val halftime: Boolean?,
    @SerializedName("short") val short: Int?,
    @SerializedName("long") val long: String?
)

data class GamePeriods(
    @SerializedName("current") val current: Int?,
    @SerializedName("total") val total: Int?,
    @SerializedName("endOfPeriod") val endOfPeriod: Boolean?
)

data class GameArena(
    @SerializedName("name") val name: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?
)