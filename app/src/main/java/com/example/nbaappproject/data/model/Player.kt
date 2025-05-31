package com.example.nbaappproject.data.model

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("playerId") val id: Int,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("teamId") val teamId: Int,
    @SerializedName("yearsPro") val yearsPro: String,
    @SerializedName("college") val college: String?,
    @SerializedName("birth") val birth: Birth,
    @SerializedName("height") val height: Height, // Zmieniono na obiekt Height
    @SerializedName("weight") val weight: String,
    @SerializedName("position") val position: String
)

data class Birth(
    @SerializedName("date") val date: String,
    @SerializedName("country") val country: String
)

data class Height(
    @SerializedName("feets") val feets: String?,
    @SerializedName("inches") val inches: String?,
    @SerializedName("meters") val meters: String?
)