package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessage
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SocketRepository,
    private val jsonUtils: JsonUtils
) : BaseViewModel() {

    private val _liveIsConnected = MutableLiveData<Boolean>()
    val liveIsConnected: LiveData<Boolean>
        get() = _liveIsConnected

    private val _liveUnAuthorized = MutableLiveData<ReceiveMessage>()
    val liveUnAuthorized: LiveData<ReceiveMessage>
        get() = _liveUnAuthorized

    private val _liveError = MutableLiveData<ReceiveMessage>()
    val liveError: LiveData<ReceiveMessage>
        get() = _liveError

    private val _liveErrorConvert = MutableLiveData<String>()
    val liveErrorConvert: LiveData<String>
        get() = _liveErrorConvert

    init {
        repository.isConnected = {
            _liveIsConnected.postValue(it)
        }
    }

    fun receiveUnAuthorized() {
        repository.onReceivedData(Constance.UNAUTHORIZED) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessage>(eventData.toString())
            receiveError.onSuccess {
                _liveUnAuthorized.postValue(it)
            }.onFailure {
                _liveErrorConvert.postValue(it.message)
            }
        }
    }

    fun receiveError() {
        repository.onReceivedData(Constance.ERROR) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessage>(eventData.toString())
            receiveError.onSuccess {
                _liveError.postValue(it)
            }.onFailure {
                _liveErrorConvert.postValue(it.message)
            }
        }
    }

    fun connect() {
        repository.connect()
    }

    fun disconnect() {
        repository.disconnect()
    }

}