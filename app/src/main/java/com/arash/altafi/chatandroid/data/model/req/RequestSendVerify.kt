package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestSendVerify(
    @SerializedName("phone")
    val phone: String,

    @SerializedName("code")
    val code: String
)