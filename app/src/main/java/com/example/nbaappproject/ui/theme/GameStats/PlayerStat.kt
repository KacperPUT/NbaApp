package com.example.nbaappproject.ui.theme.GameStats

data class PlayerStat(
    val name: String,
    val min: String,
    val pts: Int,
    val reb: Int,
    val ast: Int,
    val stl: Int,
    val blk: Int,
    val tov: Int,
    val fgPct: Double,
    val threePct: Double,
    val ftPct: Double
)
