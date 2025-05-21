package com.example.nbaappproject.data.model

import com.example.nbaappproject.data.response.GameItem

fun GameItem.toGame(): Game {
    return Game(
        id = this.id,
        date = this.date.start,
        homeTeam = Team(
            id = this.teams.home.id,
            name = this.teams.home.name,
            nickname = "", // Not directly available here
            logoUrl = this.teams.home.logo ?: "",
            city = "", // Not directly available here
            conference = "", // Not directly available here
            division = "", // Not directly available here
            code = this.teams.home.code ?: ""
        ),
        awayTeam = Team(
            id = this.teams.visitors.id,
            name = this.teams.visitors.name,
            nickname = "", // Not directly available here
            logoUrl = this.teams.visitors.logo ?: "",
            city = "", // Not directly available here
            conference = "", // Not directly available here
            division = "", // Not directly available here
            code = this.teams.visitors.code ?: ""
        ),
        homeScore = this.scores.home.points,
        awayScore = this.scores.visitors.points
    )
}