package com.example.nbaappproject.data

import com.example.nbaappproject.data.response.TeamsResponse
import com.example.nbaappproject.data.model.Team

class TeamsRepository {

    suspend fun getTeamNames(): Map<Int, String> {
        val response = RetrofitInstance.api.getTeams() // używamy getTeams(), nie getAllTeams()
        return if (response.isSuccessful) {
            response.body()?.teams?.associate { it.id to it.name } ?: emptyMap()
        } else {
            emptyMap()
        }
    }
}
