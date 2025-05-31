package com.example.nbaappproject.data.model

data class PlayerUi(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val teamName: String,
    val position: String?,
    val height: String, // Zmieniamy typ na String, bo będziemy wyciągać metry
    val weight: String,
    val birthDate: String,
    val college: String?,
    val country: String,
    val startNba: String
)

fun Player.toUi(teamName: String): PlayerUi {
    return PlayerUi(
        id = id,
        firstName = firstName,
        lastName = lastName,
        teamName = teamName,
        position = position,
        height = this.height.meters ?: "N/A", // Wyciągamy metry z obiektu Height
        weight = weight,
        birthDate = birth.date,
        college = college,
        country = birth.country,
        startNba = yearsPro
    )
}