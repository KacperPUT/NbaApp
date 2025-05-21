package com.example.nbaappproject.ui.theme.GameStats

data class TeamGameStats(
    val name: String,
    val quarters: List<Int>,
    val total: Int,
    val logoUrl: String
)
