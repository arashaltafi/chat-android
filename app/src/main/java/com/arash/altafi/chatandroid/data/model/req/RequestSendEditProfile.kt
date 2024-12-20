package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestSendEditProfile(
    @SerializedName("token")
    val token: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("family")
    val family: String,

    @SerializedName("bio")
    val bio: String,
)