package com.example.nbaappproject.data.model

import com.google.gson.annotations.SerializedName

data class Standing(
    @SerializedName("teamId") val teamId: Int,
    @SerializedName("conference") val conference: Conference,
    @SerializedName("division") val division: Division,
    @SerializedName("win") val win: Win,
    @SerializedName("loss") val loss: Loss,
    @SerializedName("streak") val streak: Int,
    @SerializedName("winStreak") val winStreak: Boolean
)

data class Conference(
    @SerializedName("name") val name: String,
    @SerializedName("rank") val rank: Int
)

data class Division(
    @SerializedName("name") val name: String,
    @SerializedName("rank") val rank: Int
)

data class Win(
    @SerializedName("total") val total: Int,
    @SerializedName("percentage") val percentage: String
)

data class Loss(
    @SerializedName("total") val total: Int
)
