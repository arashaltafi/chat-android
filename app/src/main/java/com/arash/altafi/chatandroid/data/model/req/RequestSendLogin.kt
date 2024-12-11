package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestSendLogin(
    @SerializedName("phone")
    val phone: String,
)