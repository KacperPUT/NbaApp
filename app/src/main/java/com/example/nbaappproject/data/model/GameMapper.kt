package com.example.nbaappproject.data.model

import com.example.nbaappproject.data.response.GameItem

fun GameItem.toGame(): Game {
    return Game(
        id = this.id,
        date = this.date.start,
        homeTeam = Team(
            id = this.teams.home.id,
            name = this.teams.home.name,
            nickname = "",
            logoUrl = this.teams.home.logo ?: "",
            city = "",
            conference = "",
            division = "",
            code = this.teams.home.code ?: ""
        ),
        awayTeam = Team(
            id = this.teams.visitors.id,
            name = this.teams.visitors.name,
            nickname = "",
            logoUrl = this.teams.visitors.logo ?: "",
            city = "",
            conference = "",
            division = "",
            code = this.teams.visitors.code ?: ""
        ),
        homeScore = this.scores.home.points,
        awayScore = this.scores.visitors.points
    )
}