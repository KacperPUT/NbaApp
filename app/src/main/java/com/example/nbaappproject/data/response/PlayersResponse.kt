package com.example.nbaappproject.data.response

import com.google.gson.annotations.SerializedName
import com.example.nbaappproject.data.model.Player

data class PlayersResponse(
    @SerializedName("response") val players: List<Player>
)