package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SocketRepository
) : BaseViewModel() {

    private val _liveIsConnected = MutableLiveData<Boolean>()
    val liveIsConnected: LiveData<Boolean>
        get() = _liveIsConnected

    init {
        repository.isConnected = {
            _liveIsConnected.postValue(it)
        }
    }

    fun connect() {
        repository.connect()
    }

    fun disconnect() {
        repository.disconnect()
    }

}