package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestIntroduce(
    @SerializedName("name")
    val name: String,
)