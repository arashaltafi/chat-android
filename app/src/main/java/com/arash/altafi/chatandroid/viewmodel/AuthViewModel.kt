package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arash.altafi.chatandroid.data.model.req.RequestIntroduce
import com.arash.altafi.chatandroid.data.model.req.RequestSendLogin
import com.arash.altafi.chatandroid.data.model.req.RequestSendLogout
import com.arash.altafi.chatandroid.data.model.req.RequestSendRegister
import com.arash.altafi.chatandroid.data.model.req.RequestSendVerify
import com.arash.altafi.chatandroid.data.model.res.ReceiveIntroduce
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessage
import com.arash.altafi.chatandroid.data.model.res.ReceiveVerify
import com.arash.altafi.chatandroid.data.repository.DataStoreRepository
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import com.arash.altafi.chatandroid.utils.ext.viewModelIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: SocketRepository,
    private val dataStoreRepository: DataStoreRepository,
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

    private val _liveIntroduce = MutableStateFlow<ReceiveIntroduce?>(null)
    val liveIntroduce: StateFlow<ReceiveIntroduce?>
        get() = _liveIntroduce

    private var introduceSentSuccessfully = false

    init {
        repository.isConnected = { isConnected ->
            if (isConnected && !introduceSentSuccessfully) {
                sendIntroduce()
            }
        }
    }

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

    fun sendLogout() {
        val requestSendLogout = RequestSendLogout(
            token = dataStoreRepository.getTokenString()
        )

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

    fun sendIntroduce() {
        if (introduceSentSuccessfully) return

        val requestIntroduce = RequestIntroduce(
            token = dataStoreRepository.getTokenString()
        )

        repository.emitAndReceive(
            Constance.INTRODUCE,
            jsonUtils.toCustomJson(requestIntroduce)
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveIntroduce>(eventData.toString())
            receiveError.onSuccess {
                _liveIntroduce.value = it
                introduceSentSuccessfully = true
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

    fun resetIntroduceState() {
        _liveIntroduce.value = null
    }
}