package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import com.arash.altafi.myapplication1.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : BaseViewModel() {
    private val _cachedToken = MutableLiveData<String>()
    val cachedToken: LiveData<String>
        get() = _cachedToken

    private val _cachedName = MutableLiveData<String>()
    val cachedName: LiveData<String>
        get() = _cachedName

    private val _cachedTheme = MutableLiveData<String>()
    val cachedTheme: LiveData<String>
        get() = _cachedTheme

    private val _liveError = MutableLiveData<Boolean>()
    val liveError: LiveData<Boolean>
        get() = _liveError

    private val _liveLoading = MutableLiveData<Boolean>()
    val liveLoading: LiveData<Boolean>
        get() = _liveLoading

    init {
        getToken()
        getName()
        getTheme()
    }

    // Token
    fun getToken() = callCache(
        cacheCall = repository.getToken(),
        liveResult = _cachedToken,
        liveError = _liveError,
        liveLoading = _liveLoading
    )
    fun setToken(value: String) = callCache(
        cacheCall = repository.setToken(value),
        liveResult = null,
        liveError = _liveError,
        liveLoading = _liveLoading
    )

    // Name
    fun getName() = callCache(
        cacheCall = repository.getName(),
        liveResult = _cachedName,
        liveError = _liveError,
        liveLoading = _liveLoading
    )
    fun setName(value: String) = callCache(
        cacheCall = repository.setName(value),
        liveResult = null,
        liveError = _liveError,
        liveLoading = _liveLoading
    )

    // Theme
    fun getTheme() = callCache(
        cacheCall = repository.getTheme(),
        liveResult = _cachedTheme,
        liveError = _liveError,
        liveLoading = _liveLoading
    )
    fun setTheme(theme: String) = callCache(
        cacheCall = repository.setTheme(theme),
        liveResult = null,
        liveError = _liveError,
        liveLoading = _liveLoading
    )
    fun changeTheme() = callCache(
        cacheCall = repository.changeTheme(),
        liveResult = null,
        liveError = _liveError,
        liveLoading = _liveLoading
    )
}