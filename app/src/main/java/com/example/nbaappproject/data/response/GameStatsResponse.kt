package com.example.nbaappproject.data.response

import com.google.gson.annotations.SerializedName

data class GameStatisticsResponse(
    @SerializedName("response") val response: List<GameStatisticItem>
)

data class GameStatisticItem(
    @SerializedName("team") val team: GameStatisticTeam,
    @SerializedName("statistics") val statistics: List<GameStatisticDetails>
)

data class GameStatisticTeam(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("code") val code: String,
    @SerializedName("logo") val logo: String
)

data class GameStatisticDetails(
    @SerializedName("fastBreakPoints") val fastBreakPoints: Int?,
    @SerializedName("pointsInPaint") val pointsInPaint: Int?,
    @SerializedName("biggestLead") val biggestLead: Int?,
    @SerializedName("secondChancePoints") val secondChancePoints: Int?,
    @SerializedName("pointsOffTurnovers") val pointsOffTurnovers: Int?,
    @SerializedName("longestRun") val longestRun: Int?,
    @SerializedName("points") val points: Int?,
    @SerializedName("fgm") val fgm: Int?,
    @SerializedName("fga") val fga: Int?,
    @SerializedName("fgp") val fgp: String?,
    @SerializedName("ftm") val ftm: Int?,
    @SerializedName("fta") val fta: Int?,
    @SerializedName("ftp") val ftp: String?,
    @SerializedName("tpm") val tpm: Int?,
    @SerializedName("tpa") val tpa: Int?,
    @SerializedName("tpp") val tpp: String?,
    @SerializedName("offReb") val offReb: Int?,
    @SerializedName("defReb") val defReb: Int?,
    @SerializedName("totReb") val totReb: Int?,
    @SerializedName("assists") val assists: Int?,
    @SerializedName("pFouls") val pFouls: Int?,
    @SerializedName("steals") val steals: Int?,
    @SerializedName("turnovers") val turnovers: Int?,
    @SerializedName("blocks") val blocks: Int?,
    @SerializedName("plusMinus") val plusMinus: String?,
    @SerializedName("min") val min: String?
)
