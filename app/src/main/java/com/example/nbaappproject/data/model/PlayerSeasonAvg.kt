package com.example.nbaappproject.data.model

data class PlayerSeasonAverages(
    val playerId: Int,
    val firstName: String,
    val lastName: String,
    val teamName: String,
    val gamesPlayed: Int,
    val avgPoints: Double,
    val avgAssists: Double,
    val avgTotalRebounds: Double,
    val avgOffensiveRebounds: Double,
    val avgDefensiveRebounds: Double,
    val avgSteals: Double,
    val avgBlocks: Double,
    val avgPersonalFouls: Double,
    val avgTurnovers: Double,
    val avgPlusMinus: Double,
    val avgMinutes: String,
    val avgFieldGoalPercentage: Double,
    val avgFreeThrowPercentage: Double,
    val avgThreePointPercentage: Double
)
