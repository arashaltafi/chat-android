package com.arash.altafi.chatandroid.data.repository

import com.arash.altafi.chatandroid.utils.base.BaseRepository
import com.arash.altafi.chatandroid.data.api.UploadService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val service: UploadService,
) : BaseRepository() {

    fun sendUpload(
        fileRequestBody: RequestBody,
        fileName: String
    ) = callApi {
        val filePart = MultipartBody.Part.createFormData("file", fileName, fileRequestBody)
        service.sendPhone(
            filePart
        )
    }

    fun updateUsername(
        username: String,
    ) = callApi {
        service.updateUsername(
            username
        )
    }

}

