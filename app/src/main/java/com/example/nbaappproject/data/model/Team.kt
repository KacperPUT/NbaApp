package com.example.nbaappproject.data.model

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("logo") val logoUrl: String,
    @SerializedName("city") val city: String?,
    @SerializedName("conference") val conference: String?,
    @SerializedName("division") val division: String?,
    @SerializedName("code") val code: String? = null,
    @SerializedName("nbaFranchise") val nbaFranchise: Boolean? = null
)