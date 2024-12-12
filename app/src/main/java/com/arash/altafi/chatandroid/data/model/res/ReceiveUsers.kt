package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveUsersResponse(
    @SerializedName("users")
    val users: List<ReceiveUsers>
)

data class ReceiveUsers(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("family")
    val family: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("lastSeen")
    val lastSeen: String,
)