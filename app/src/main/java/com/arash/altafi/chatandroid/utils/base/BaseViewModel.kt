package com.arash.altafi.chatandroid.utils.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

open class BaseViewModel : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    fun <T> callApi(
        networkCall: Flow<Response<T>>,
        liveResult: MutableLiveData<T>? = null,
        liveError:  MutableLiveData<Boolean>? = null,
        liveLoading:  MutableLiveData<Boolean>? = null,
        onResponse: ((T) -> Unit)? = null,
    ) {
        viewModelScope.launch {
            networkCall.onStart {
                liveLoading?.value = true
                liveError?.value = false
            }.catch {
                liveLoading?.value = false
                liveError?.value = true
            }.collect { response ->
                if (response.isSuccessful) {
                    response.body()?.let {
                        liveResult?.value = it
                        onResponse?.invoke(it)
                    }
                    liveLoading?.value = false
                    liveError?.value = false
                } else {
                    liveLoading?.value = false
                    liveError?.value = true
                }
            }
        }
    }

    fun <T> callCache(
        cacheCall: Flow<T>,
        liveResult: MutableLiveData<T>? = null,
        liveError: MutableLiveData<Boolean>? = null,
        liveLoading: MutableLiveData<Boolean>? = null,
        onResponse: ((T) -> Unit)? = null,
    ) {
        viewModelScope.launch {
            cacheCall.onStart {
                liveLoading?.value = true
                liveError?.value = false
            }.catch {
                liveLoading?.value = false
                liveError?.value = true
            }.collect { response ->
                liveResult?.value = response
                onResponse?.invoke(response)
                liveLoading?.value = false
                liveError?.value = false
            }
        }
    }

    fun <T> callDatabase(
        databaseCall: Flow<T>,
        liveResult: MutableLiveData<T>? = null,
        liveError:  MutableLiveData<Boolean>? = null,
        liveLoading:  MutableLiveData<Boolean>? = null,
        onResponse: ((T) -> Unit)? = null,
    ) {
        viewModelScope.launch {
            databaseCall.onStart {
                liveLoading?.value = true
                liveError?.value = false
            }.catch {
                liveLoading?.value = false
                liveError?.value = true
            }.collect { response ->
                liveResult?.value = response
                onResponse?.invoke(response)
                liveLoading?.value = false
                liveError?.value = false
            }
        }
    }
}