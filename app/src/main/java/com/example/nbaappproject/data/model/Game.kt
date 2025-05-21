package com.example.nbaappproject.data.model

import com.google.gson.annotations.SerializedName
import com.example.nbaappproject.data.model.Team // Używamy istniejącej klasy Team

data class Game(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("homeTeam") val homeTeam: Team,
    @SerializedName("awayTeam") val awayTeam: Team,
    @SerializedName("homeScore") val homeScore: Int,
    @SerializedName("awayScore") val awayScore: Int
)
