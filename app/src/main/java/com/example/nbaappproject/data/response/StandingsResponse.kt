package com.example.nbaappproject.data.response

import com.example.nbaappproject.data.model.Team
import com.google.gson.annotations.SerializedName

data class StandingsResponse(
    @SerializedName("response") val standings: List<Standing>
)

data class Standing(
    @SerializedName("team") val team: Team,
    @SerializedName("conference") val conference: StandingConference,
    @SerializedName("win") val wins: Record,
    @SerializedName("loss") val losses: Record
    // Dodaj inne pola jeśli potrzebne
)

data class StandingConference(
    @SerializedName("name") val name: String,
    @SerializedName("rank") val rank: Int
)

data class Record(
    @SerializedName("total") val total: Int
)
