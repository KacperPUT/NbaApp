package com.example.nbaappproject.data.response

import com.google.gson.annotations.SerializedName
import com.example.nbaappproject.ui.theme.Teams.TeamStats

data class TeamStatsResponse(
    @SerializedName("response") val stats: List<TeamStats>
)