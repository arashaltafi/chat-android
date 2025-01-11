package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.viewModelScope
import com.arash.altafi.chatandroid.data.model.req.RequestGetMessages
import com.arash.altafi.chatandroid.data.model.req.RequestSendMessageChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveGetMessages
import com.arash.altafi.chatandroid.data.model.res.ReceiveGetMessagesChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessageChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessagesPeer
import com.arash.altafi.chatandroid.data.model.res.ReceiveSendMessageChatRoom
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
class ChatViewModel @Inject constructor(
    private val repository: SocketRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    init {
        receiveMessage()
    }

    private val _liveGetMessages = MutableStateFlow<ReceiveGetMessages?>(null)
    val liveGetMessages: StateFlow<ReceiveGetMessages?>
        get() = _liveGetMessages

    private val _liveSendMessage = MutableStateFlow<ReceiveSendMessageChatRoom?>(null)
    val liveSendMessage: StateFlow<ReceiveSendMessageChatRoom?>
        get() = _liveSendMessage

    private val _liveReceiveMessage = MutableStateFlow<ReceiveMessagesPeer?>(null)
    val liveReceiveMessage: StateFlow<ReceiveMessagesPeer?>
        get() = _liveReceiveMessage

    fun getMessages(peerId: Int? = null) {
        val requestGetMessages = RequestGetMessages(
            peerId = peerId,
        )

        repository.emitAndReceive(
            Constance.GET_MESSAGES,
            jsonUtils.toCustomJson(requestGetMessages)
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveGetMessages>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveGetMessages.emit(it)
                }
            }
        }
    }

    // todo fix it
    fun sendMessages(message: String? = null, url: String? = null, reaction: String? = null) {
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
                    _liveSendMessage.emit(it)
                }
            }
        }
    }

    // todo fix it
    private fun receiveMessage() {
        repository.onReceivedData(Constance.RECEIVE_MESSAGE) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveMessagesPeer>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveReceiveMessage.emit(it)
                }
            }
        }
    }
}