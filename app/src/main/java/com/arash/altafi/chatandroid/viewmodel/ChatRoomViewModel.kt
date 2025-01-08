package com.arash.altafi.chatandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arash.altafi.chatandroid.data.model.res.ReceiveGetMessagesChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveUserStatusChatRoom
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val repository: SocketRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    init {
        getMessagesChatRoom()
        receiveUsersStatus()
    }

    private val _liveGetChatRoom = MutableLiveData<ReceiveGetMessagesChatRoom>()
    val liveGetChatRoom: LiveData<ReceiveGetMessagesChatRoom>
        get() = _liveGetChatRoom

    private val _liveUserCount = MutableLiveData<Pair<Int, Int>>()
    val liveUserCount: LiveData<Pair<Int, Int>>
        get() = _liveUserCount

    private fun getMessagesChatRoom() {
        repository.emitAndReceive(
            Constance.GET_CHAT_ROOM,
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveGetMessagesChatRoom>(eventData.toString())
            receiveError.onSuccess {
                _liveGetChatRoom.postValue(it)
            }
        }
    }

    private fun receiveUsersStatus() {
        repository.onReceivedData(Constance.USER_STATUS_CHAT_ROOM) { eventData ->
            Log.i("test123321", "receiveUsersStatus: ${eventData.toString()}")
            val receiveError =
                jsonUtils.getSafeObject<ReceiveUserStatusChatRoom>(eventData.toString())
            receiveError.onSuccess {
                _liveUserCount.postValue(Pair(it.usersOnline, it.usersCount))
            }
        }
    }

}