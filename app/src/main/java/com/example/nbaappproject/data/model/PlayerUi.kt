package com.example.nbaappproject.data.model

data class PlayerUi(
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val teamName: String,
    val jerseyNumber: Int?,
    val position: String?,
    val height: String,
    val weight: String,
    val birthDate: String?,
    val college: String?,
    val country: String?,
    val startNba: String?
)

fun Player.toUi(teamName: String): PlayerUi {
    return PlayerUi(
        id = id,
        firstName = firstName,
        lastName = lastName,
        teamName = teamName,
        jerseyNumber = leagues?.standard?.jersey,
        position = leagues?.standard?.pos,
        height = height.meters ?: "N/A",
        weight = weight.kilograms ?: "N/A",
        birthDate = birth.date,
        college = college,
        country = birth.country,
        startNba = nba.pro?.toString()
    )
}
