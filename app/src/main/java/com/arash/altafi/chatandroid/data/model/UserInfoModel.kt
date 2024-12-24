package com.arash.altafi.chatandroid.data.model

import com.google.gson.annotations.SerializedName

data class UserInfoModel(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("family")
    val family: String? = null,

    @SerializedName("avatar")
    val avatar: String? = null,

    @SerializedName("bio")
    val bio: String? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("phone")
    val phone: String? = null,
)