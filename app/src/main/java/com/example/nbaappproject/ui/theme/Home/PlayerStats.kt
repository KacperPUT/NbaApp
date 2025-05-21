package com.example.nbaappproject.ui.theme.Home

import com.google.gson.annotations.SerializedName

data class PlayerStats(
    @SerializedName("playerId") val playerId: Int,
    @SerializedName("games") val games: Int,
    @SerializedName("points") val points: Int,
    @SerializedName("assists") val assists: Int,
    @SerializedName("totReb") val totalRebounds: Int,
    @SerializedName("steals") val steals: Int,
    @SerializedName("blocks") val blocks: Int,
    @SerializedName("turnovers") val turnovers: Int,
    @SerializedName("fgp") val fieldGoalPercentage: String,
    @SerializedName("ftp") val freeThrowPercentage: String,
    @SerializedName("tpp") val threePointPercentage: String
)
