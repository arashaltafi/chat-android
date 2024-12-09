package com.arash.altafi.chatandroid.data.model.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReceiveGetMessagesModel(
    @SerializedName("message")
    val message: String?,

    @SerializedName("user")
    val user: String?,

    @SerializedName("peer")
    val peer: String?,

    @SerializedName("room")
    val room: String?,

    @SerializedName("dir")
    val dir: Boolean?,
) : Parcelable