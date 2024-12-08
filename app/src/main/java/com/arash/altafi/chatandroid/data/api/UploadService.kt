package com.arash.altafi.chatandroid.data.api

import com.arash.altafi.chatandroid.data.model.ReceiveUploadModel
import com.arash.altafi.chatandroid.utils.base.BaseService
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UploadService : BaseService {

    @Multipart
    @POST("files")
    suspend fun sendPhone(
        @Part filePart: MultipartBody.Part
    ): Response<ReceiveUploadModel>

    @POST("files")
    suspend fun updateUsername(
        @Query("username") username: String
    ): Response<ReceiveUploadModel>

}