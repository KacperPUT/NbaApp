package com.example.nbaappproject.data.response

import com.example.nbaappproject.data.model.Team
import com.google.gson.annotations.SerializedName

data class StandingsResponse(
    @SerializedName("response") val standings: List<StandingResponseItem>
)

data class StandingResponseItem(
    @SerializedName("league") val league: String,
    @SerializedName("season") val season: Int,
    @SerializedName("team") val team: Team,
    @SerializedName("conference") val conference: ApiConference,
    @SerializedName("division") val division: ApiDivision?,
    @SerializedName("win") val win: ApiRecordDetails,
    @SerializedName("loss") val loss: ApiRecordDetails,
    @SerializedName("gamesBehind") val gamesBehind: String?,
    @SerializedName("streak") val streak: Int,
    @SerializedName("winStreak") val winStreak: Boolean,
    @SerializedName("tieBreakerPoints") val tieBreakerPoints: String?
)

data class ApiConference(
    @SerializedName("name") val name: String,
    @SerializedName("rank") val rank: Int,
    @SerializedName("win") val win: Int, // Dodano win dla konferencji (z odpowiedzi API)
    @SerializedName("loss") val loss: Int // Dodano loss dla konferencji (z odpowiedzi API)
)

data class ApiDivision(
    @SerializedName("name") val name: String,
    @SerializedName("rank") val rank: Int,
    @SerializedName("win") val win: Int?,
    @SerializedName("loss") val loss: Int?,
    @SerializedName("gamesBehind") val gamesBehind: String?
)

data class ApiRecordDetails(
    @SerializedName("home") val home: Int,
    @SerializedName("away") val away: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("percentage") val percentage: String,
    @SerializedName("lastTen") val lastTen: Int
)

data class Team( // Zakładam, że Twój model Team ma te pola
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("code") val code: String,
    @SerializedName("logo") val logoUrl: String // Zmieniono nazwę na bardziej opisową
)