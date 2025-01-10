package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.viewModelScope
import com.arash.altafi.chatandroid.data.model.req.RequestSendMessageChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveGetMessagesChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessage
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessageChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveSendMessageChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveUserStatusChatRoom
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val repository: SocketRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    init {
        getMessagesChatRoom()
        receiveUsersStatus()
        receiveMessageChatRoom()
    }

    private val _liveGetChatRoom = MutableStateFlow<ReceiveGetMessagesChatRoom?>(null)
    val liveGetChatRoom: StateFlow<ReceiveGetMessagesChatRoom?>
        get() = _liveGetChatRoom

    private val _liveSendChatRoom = MutableStateFlow<ReceiveSendMessageChatRoom?>(null)
    val liveSendChatRoom: StateFlow<ReceiveSendMessageChatRoom?>
        get() = _liveSendChatRoom

    private val _liverMessageChatRoom = MutableStateFlow<ReceiveMessageChatRoom?>(null)
    val liverMessageChatRoom: StateFlow<ReceiveMessageChatRoom?>
        get() = _liverMessageChatRoom

    private val _liveUserCount = MutableStateFlow<Pair<Int, Int>?>(null)
    val liveUserCount: StateFlow<Pair<Int, Int>?>
        get() = _liveUserCount

    private fun getMessagesChatRoom() {
        repository.emitAndReceive(
            Constance.GET_CHAT_ROOM,
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveGetMessagesChatRoom>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveGetChatRoom.emit(it)
                }
            }
        }
    }

    private fun sendMessagesChatRoom(message: String? = null, url: String? = null) {
        val requestSendMessageChatRoom = RequestSendMessageChatRoom(
            text = message,
            url = url,
        )

        repository.emitAndReceive(
            Constance.SEND_CHAT_ROOM,
            jsonUtils.toCustomJson(requestSendMessageChatRoom)
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveSendMessageChatRoom>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveSendChatRoom.emit(it)
                }
            }
        }
    }

    private fun receiveMessageChatRoom() {
        repository.onReceivedData(Constance.RECEIVE_MESSAGE_CHAT_ROOM) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessageChatRoom>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liverMessageChatRoom.emit(it)
                }
            }
        }
    }

    private fun receiveUsersStatus() {
        repository.onReceivedData(Constance.USER_STATUS_CHAT_ROOM) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveUserStatusChatRoom>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveUserCount.emit(Pair(it.usersOnline, it.usersCount))
                }
            }
        }
    }

}