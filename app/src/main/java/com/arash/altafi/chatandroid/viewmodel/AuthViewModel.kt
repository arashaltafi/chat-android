package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arash.altafi.chatandroid.data.model.req.RequestSendLogin
import com.arash.altafi.chatandroid.data.model.req.RequestSendLogout
import com.arash.altafi.chatandroid.data.model.req.RequestSendRegister
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessage
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

    private val _liveLogin = MutableLiveData<ReceiveMessage>()
    val liveLogin: LiveData<ReceiveMessage>
        get() = _liveLogin

    private val _liveLogout = MutableLiveData<Any>()
    val liveLogout: LiveData<Any>
        get() = _liveLogout

    private val _liveRegister = MutableLiveData<Any>()
    val liveRegister: LiveData<Any>
        get() = _liveRegister

    fun sendLogin(phone: String) {
        val requestSendLogin = RequestSendLogin(phone = phone)

        repository.emitAndReceive(
            Constance.LOGIN,
            jsonUtils.toCustomJson(requestSendLogin)
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessage>(eventData.toString())
            receiveError.onSuccess {
                _liveLogin.postValue(it)
            }
        }
    }

    fun sendRegister(phone: String, name: String, family: String) {
        val requestSendRegister = RequestSendRegister(
            phone = phone,
            name = name,
            family = family,
        )

        repository.emitAndReceive(
            Constance.REGISTER,
            jsonUtils.toCustomJson(requestSendRegister)
        ) { eventData ->
            _liveRegister.postValue(eventData)
        }
    }

    fun sendLogout(token: String) {
        val requestSendLogout = RequestSendLogout(token = token)

        repository.emitAndReceive(
            Constance.LOGOUT,
            jsonUtils.toCustomJson(requestSendLogout)
        ) { eventData ->
            _liveLogout.postValue(eventData)
        }
    }
}