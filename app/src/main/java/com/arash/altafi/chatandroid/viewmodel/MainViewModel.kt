package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessage
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SocketRepository,
    private val jsonUtils: JsonUtils
) : BaseViewModel() {

    private val _liveIsConnected = MutableStateFlow<Boolean?>(null)
    val liveIsConnected: StateFlow<Boolean?>
        get() = _liveIsConnected

    private val _liveUnAuthorized = MutableStateFlow<ReceiveMessage?>(null)
    val liveUnAuthorized: StateFlow<ReceiveMessage?>
        get() = _liveUnAuthorized

    private val _liveError = MutableStateFlow<ReceiveMessage?>(null)
    val liveError: StateFlow<ReceiveMessage?>
        get() = _liveError

    private val _liveErrorConvert = MutableStateFlow<String?>(null)
    val liveErrorConvert: StateFlow<String?>
        get() = _liveErrorConvert

    init {
        repository.isConnected = {
            _liveIsConnected.value = it
        }
    }

    fun receiveUnAuthorized() {
        repository.onReceivedData(Constance.UNAUTHORIZED) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessage>(eventData.toString())
            receiveError.onSuccess {
                _liveUnAuthorized.value = it
            }.onFailure {
                _liveErrorConvert.value = it.message
            }
        }
    }

    fun receiveError() {
        repository.onReceivedData(Constance.ERROR) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessage>(eventData.toString())
            receiveError.onSuccess {
                _liveError.value = it
            }.onFailure {
                _liveErrorConvert.value = it.message
            }
        }
    }

    fun connect() {
        repository.connect()
    }

    fun disconnect() {
        repository.disconnect()
    }

    fun resetErrorState() {
        _liveError.value = null
    }

    fun resetUnAuthorizedState() {
        _liveUnAuthorized.value = null
    }
}