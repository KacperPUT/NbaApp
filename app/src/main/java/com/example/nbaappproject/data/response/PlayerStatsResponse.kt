package com.example.nbaappproject.data.response

import com.google.gson.annotations.SerializedName

data class PlayerStatsResponse(
    @SerializedName("response") val playerStats: List<PlayerStatsItem>
)

data class PlayerStatsItem(
    @SerializedName("player") val player: PlayerInfo,
    @SerializedName("team") val team: TeamInfo,
    @SerializedName("game") val game: GameInfo,
    @SerializedName("points") val points: Int?,
    @SerializedName("pos") val position: String?,
    @SerializedName("min") val minutes: String?,
    @SerializedName("fgm") val fieldGoalsMade: Int?,
    @SerializedName("fga") val fieldGoalsAttempted: Int?,
    @SerializedName("fgp") val fieldGoalPercentage: String?,
    @SerializedName("ftm") val freeThrowsMade: Int?,
    @SerializedName("fta") val freeThrowsAttempted: Int?,
    @SerializedName("ftp") val freeThrowPercentage: String?,
    @SerializedName("tpm") val threePointsMade: Int?,
    @SerializedName("tpa") val threePointsAttempted: Int?,
    @SerializedName("tpp") val threePointPercentage: String?,
    @SerializedName("offReb") val offensiveRebounds: Int?,
    @SerializedName("defReb") val defensiveRebounds: Int?,
    @SerializedName("totReb") val totalRebounds: Int?,
    @SerializedName("assists") val assists: Int?,
    @SerializedName("pFouls") val personalFouls: Int?,
    @SerializedName("steals") val steals: Int?,
    @SerializedName("turnovers") val turnovers: Int?,
    @SerializedName("blocks") val blocks: Int?,
    @SerializedName("plusMinus") val plusMinus: String?,
    @SerializedName("comment") val comment: String?
)

data class PlayerInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("lastname") val lastname: String
)

data class TeamInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("code") val code: String,
    @SerializedName("logo") val logo: String
)

data class GameInfo(
    @SerializedName("id") val id: Int
)