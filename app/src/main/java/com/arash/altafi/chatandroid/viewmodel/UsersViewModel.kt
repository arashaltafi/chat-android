package com.arash.altafi.chatandroid.viewmodel

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
class UsersViewModel @Inject constructor(
    private val repository: SocketRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    private val _liveGetUsers = MutableStateFlow<List<ReceiveMessage>?>(null)
    val liveGetUsers: StateFlow<List<ReceiveMessage>?>
        get() = _liveGetUsers

    fun getUsers() {
        repository.emitAndReceive(
            Constance.USERS,
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<List<ReceiveMessage>>(eventData.toString())
            receiveError.onSuccess {
                _liveGetUsers.value = it
            }
        }
    }
}