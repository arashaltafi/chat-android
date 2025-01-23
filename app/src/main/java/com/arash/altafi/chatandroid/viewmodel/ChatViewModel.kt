package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.viewModelScope
import com.arash.altafi.chatandroid.data.model.req.RequestGetMessages
import com.arash.altafi.chatandroid.data.model.req.RequestReactionMessage
import com.arash.altafi.chatandroid.data.model.req.RequestSendMessageChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveDeleteDialog
import com.arash.altafi.chatandroid.data.model.res.ReceiveGetMessages
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessagesPeer
import com.arash.altafi.chatandroid.data.model.res.ReceiveReactionMessage
import com.arash.altafi.chatandroid.data.model.res.ReceiveSendMessageChatRoom
import com.arash.altafi.chatandroid.data.model.res.ReceiveTyping
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
        receiveSeenMessage()
        receiveDeliverMessage()
        receiveReactionMessage()
        receiveTyping()
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

    private val _liveSeenMessage = MutableStateFlow<ReceiveDeleteDialog?>(null)
    val liveSeenMessage: StateFlow<ReceiveDeleteDialog?>
        get() = _liveSeenMessage

    private val _liveDeliverMessage = MutableStateFlow<ReceiveDeleteDialog?>(null)
    val liveDeliverMessage: StateFlow<ReceiveDeleteDialog?>
        get() = _liveDeliverMessage

    private val _liveReactionMessage = MutableStateFlow<ReceiveReactionMessage?>(null)
    val liveReactionMessage: StateFlow<ReceiveReactionMessage?>
        get() = _liveReactionMessage

    private val _liveTyping = MutableStateFlow<ReceiveTyping?>(null)
    val liveTyping: StateFlow<ReceiveTyping?>
        get() = _liveTyping

    fun getMessages(peerId: Int) {
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

    fun sendSeenMessage(peerId: Int) {
        val requestGetMessages = RequestGetMessages(
            peerId = peerId,
        )

        repository.send(
            Constance.SEEN_MESSAGE,
            jsonUtils.toCustomJson(requestGetMessages)
        )
    }

    fun sendStartTyping(peerId: Int) {
        val requestGetMessages = RequestGetMessages(
            peerId = peerId,
        )

        repository.send(
            Constance.START_TYPING,
            jsonUtils.toCustomJson(requestGetMessages)
        )
    }

    fun sendStopTyping(peerId: Int) {
        val requestGetMessages = RequestGetMessages(
            peerId = peerId,
        )

        repository.send(
            Constance.STOP_TYPING,
            jsonUtils.toCustomJson(requestGetMessages)
        )
    }

    fun sendReactionMessage(peerId: Int, messageId: Long, reaction: String) {
        val requestReactionMessage = RequestReactionMessage(
            peerId = peerId,
            messageId = messageId,
            reaction = reaction,
        )

        repository.send(
            Constance.REACTION_MESSAGE,
            jsonUtils.toCustomJson(requestReactionMessage)
        )
    }

    // todo fix it
    fun sendMessages(peerId: Int, message: String? = null, url: String? = null) {
        sendStopTyping(peerId)

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

    private fun receiveDeliverMessage() {
        repository.onReceivedData(Constance.DELIVER_MESSAGE) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveDeleteDialog>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveDeliverMessage.emit(it)
                }
            }
        }
    }

    private fun receiveSeenMessage() {
        repository.onReceivedData(Constance.SEEN_MESSAGE) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveDeleteDialog>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveSeenMessage.emit(it)
                }
            }
        }
    }

    private fun receiveReactionMessage() {
        repository.onReceivedData(Constance.REACTION_MESSAGE) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveReactionMessage>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveReactionMessage.emit(it)
                }
            }
        }
    }

    private fun receiveTyping() {
        repository.onReceivedData(Constance.IS_TYPING) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveTyping>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveTyping.emit(it)
                }
            }
        }
    }
}