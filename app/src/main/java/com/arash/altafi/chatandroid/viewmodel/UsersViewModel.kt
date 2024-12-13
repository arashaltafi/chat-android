package com.arash.altafi.chatandroid.viewmodel

import com.arash.altafi.chatandroid.data.model.res.ReceiveUsersResponse
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

    private val _liveGetUsers = MutableStateFlow<ReceiveUsersResponse?>(null)
    val liveGetUsers: StateFlow<ReceiveUsersResponse?>
        get() = _liveGetUsers

    fun getUsers() {
        repository.emitAndReceive(
            Constance.USERS,
        ) { eventData ->
            val receiveUsers =
                jsonUtils.getSafeObject<ReceiveUsersResponse>(eventData.toString())
            receiveUsers.onSuccess {
                _liveGetUsers.value = it
            }
        }
    }
}