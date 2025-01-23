package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveDeleteDialog(
    @SerializedName("peerId")
    val peerId: Int? = null,
)