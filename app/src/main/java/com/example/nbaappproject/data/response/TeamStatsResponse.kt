package com.example.nbaappproject.data.response

import com.google.gson.annotations.SerializedName

data class TeamStatsResponse(
    @SerializedName("response") val stats: List<TeamStatsItem> // Zmieniamy na List<TeamStatsItem>
)

data class TeamStatsItem(
    val games: Int,
    @SerializedName("fastBreakPoints") val fastBreakPoints: Int,
    @SerializedName("pointsInPaint") val pointsInPaint: Int,
    @SerializedName("biggestLead") val biggestLead: Int,
    @SerializedName("secondChancePoints") val secondChancePoints: Int,
    @SerializedName("pointsOffTurnovers") val pointsOffTurnovers: Int,
    @SerializedName("longestRun") val longestRun: Int,
    val points: Int,
    val fgm: Int,
    val fga: Int,
    @SerializedName("fgp") val fieldGoalPercentage: String,
    val ftm: Int,
    val fta: Int,
    @SerializedName("ftp") val freeThrowPercentage: String,
    val tpm: Int,
    val tpa: Int,
    @SerializedName("tpp") val threePointPercentage: String,
    @SerializedName("offReb") val offensiveRebounds: Int,
    @SerializedName("defReb") val defensiveRebounds: Int,
    @SerializedName("totReb") val totalRebounds: Int,
    val assists: Int,
    @SerializedName("pFouls") val personalFouls: Int,
    val steals: Int,
    val turnovers: Int,
    val blocks: Int,
    @SerializedName("plusMinus") val plusMinus: Int
)