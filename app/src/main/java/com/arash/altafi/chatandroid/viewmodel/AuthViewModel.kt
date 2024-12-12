package com.arash.altafi.chatandroid.viewmodel

import com.arash.altafi.chatandroid.data.model.req.RequestSendLogin
import com.arash.altafi.chatandroid.data.model.req.RequestSendLogout
import com.arash.altafi.chatandroid.data.model.req.RequestSendRegister
import com.arash.altafi.chatandroid.data.model.req.RequestSendVerify
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessage
import com.arash.altafi.chatandroid.data.model.res.ReceiveVerify
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: SocketRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    private val _liveLogin = MutableStateFlow<ReceiveMessage?>(null)
    val liveLogin: StateFlow<ReceiveMessage?>
        get() = _liveLogin

    private val _liveLogout = MutableStateFlow<ReceiveMessage?>(null)
    val liveLogout: StateFlow<ReceiveMessage?>
        get() = _liveLogout

    private val _liveRegister = MutableStateFlow<ReceiveMessage?>(null)
    val liveRegister: StateFlow<ReceiveMessage?>
        get() = _liveRegister

    private val _liveVerify = MutableStateFlow<ReceiveVerify?>(null)
    val liveVerify: StateFlow<ReceiveVerify?>
        get() = _liveVerify

    fun sendLogin(phone: String) {
        val requestSendLogin = RequestSendLogin(phone = phone)

        repository.emitAndReceive(
            Constance.LOGIN,
            jsonUtils.toCustomJson(requestSendLogin)
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessage>(eventData.toString())
            receiveError.onSuccess {
                _liveLogin.value = it
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
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessage>(eventData.toString())
            receiveError.onSuccess {
                _liveRegister.value = it
            }
        }
    }

    fun sendVerify(phone: String, code: String) {
        val requestSendVerify = RequestSendVerify(
            phone = phone,
            code = code,
        )

        repository.emitAndReceive(
            Constance.VERIFY,
            jsonUtils.toCustomJson(requestSendVerify)
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveVerify>(eventData.toString())
            receiveError.onSuccess {
                _liveVerify.value = it
            }
        }
    }

    fun sendLogout(token: String) {
        val requestSendLogout = RequestSendLogout(token = token)

        repository.emitAndReceive(
            Constance.LOGOUT,
            jsonUtils.toCustomJson(requestSendLogout)
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessage>(eventData.toString())
            receiveError.onSuccess {
                _liveLogout.value = it
            }
        }
    }

    fun resetLoginState() {
        _liveLogin.value = null
    }

    fun resetLogoutState() {
        _liveLogout.value = null
    }

    fun resetRegisterState() {
        _liveRegister.value = null
    }
}