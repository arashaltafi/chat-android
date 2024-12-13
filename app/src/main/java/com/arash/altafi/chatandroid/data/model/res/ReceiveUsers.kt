package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveUsersResponse(
    @SerializedName("users")
    val users: List<ReceiveUsers>
)

data class ReceiveUsers(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("family")
    val family: String? = null,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("lastSeen")
    val lastSeen: String? = null,
)