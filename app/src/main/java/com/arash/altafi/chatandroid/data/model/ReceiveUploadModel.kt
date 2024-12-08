package com.arash.altafi.chatandroid.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReceiveUploadModel(
    @SerializedName("message")
    val message: String?,

    @SerializedName("file")
    val file: File?,

    ) : Parcelable {

    @Parcelize
    data class File(
        @SerializedName("hashCode")
        val hashCode: String?,

        @SerializedName("originalName")
        val originalName: String?,

        @SerializedName("size")
        val size: String?,

        @SerializedName("timestamp")
        val timestamp: String?,

        @SerializedName("mimeType")
        val mimeType: String?,
    ) : Parcelable

}