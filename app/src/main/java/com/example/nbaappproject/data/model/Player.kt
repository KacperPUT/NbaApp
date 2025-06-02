package com.example.nbaappproject.data.model

import com.google.gson.annotations.SerializedName

data class Player(
    val id: Int,
    @SerializedName("firstname") val firstName: String?,
    @SerializedName("lastname") val lastName: String?,
    @SerializedName("birth") val birth: Birth,
    @SerializedName("nba") val nba: Nba,
    @SerializedName("height") val height: Height,
    @SerializedName("weight") val weight: Weight, // mała litera 'w'
    @SerializedName("college") val college: String?,
    @SerializedName("affiliation") val affiliation: String?,
    @SerializedName("leagues") val leagues: Leagues?
)

data class Birth(
    val date: String?,
    val country: String?
)

data class Nba(
    val start: Int?,
    val pro: Int?
)

data class Height(
    val feets: String?,
    val inches: String?,
    val meters: String?
)

data class Weight(
    val pounds: String?,
    val kilograms: String?
)

data class Leagues(
    val standard: StandardLeague?
)

data class StandardLeague(
    val jersey: Int?,
    val active: Boolean?,
    val pos: String?
)
