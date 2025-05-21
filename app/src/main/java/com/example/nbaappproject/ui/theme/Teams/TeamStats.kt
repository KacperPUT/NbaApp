package com.example.nbaappproject.ui.theme.Teams

import com.google.gson.annotations.SerializedName

data class TeamStats(
    @SerializedName("teamId") val teamId: Int,
    @SerializedName("games") val games: Int,
    @SerializedName("fastBreakPoints") val fastBreakPoints: Int,
    @SerializedName("pointsInPaint") val pointsInPaint: Int,
    @SerializedName("biggestLead") val biggestLead: Int,
    @SerializedName("points") val points: Int,
    @SerializedName("fgp") val fieldGoalPercentage: String,
    @SerializedName("ftp") val freeThrowPercentage: String,
    @SerializedName("tpp") val threePointPercentage: String,
    @SerializedName("assists") val assists: Int,
    @SerializedName("totReb") val totalRebounds: Int,
    @SerializedName("steals") val steals: Int,
    @SerializedName("blocks") val blocks: Int,
    @SerializedName("turnovers") val turnovers: Int
)
