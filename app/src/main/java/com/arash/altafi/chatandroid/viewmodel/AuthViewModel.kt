package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arash.altafi.chatandroid.data.model.req.RequestSendLogin
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: SocketRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    private val _liveLogin = MutableLiveData<Any>()
    val liveLogin: LiveData<Any>
        get() = _liveLogin

    fun sendLogin(phone: String) {
        val requestSendLogin = RequestSendLogin(phone = phone)

        repository.emitAndReceive(
            Constance.INTRODUCE,
            jsonUtils.toCustomJson(requestSendLogin)
        ) { eventData ->
            _liveLogin.postValue(eventData)
        }
    }
}