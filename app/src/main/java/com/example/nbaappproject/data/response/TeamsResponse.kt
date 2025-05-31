package com.example.nbaappproject.data.response

import com.example.nbaappproject.data.model.Team
import com.google.gson.annotations.SerializedName

data class TeamsResponse(
    @SerializedName("response") val teams: List<Team>
)