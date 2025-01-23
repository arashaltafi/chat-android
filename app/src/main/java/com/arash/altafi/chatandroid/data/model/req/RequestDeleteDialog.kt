package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestDeleteDialog(
    @SerializedName("peerId")
    val peerId: Int? = null,
    @SerializedName("deleteBoth")
    val deleteBoth: Boolean? = null,
)