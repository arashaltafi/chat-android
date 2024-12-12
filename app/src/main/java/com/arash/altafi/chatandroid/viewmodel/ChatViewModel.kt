package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arash.altafi.chatandroid.data.model.req.RequestGetMessages
import com.arash.altafi.chatandroid.data.model.req.RequestSendMessage
import com.arash.altafi.chatandroid.data.model.res.ReceiveGetMessagesModel
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import com.arash.altafi.chatandroid.utils.ext.viewModelIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: SocketRepository,
    private var jsonUtils: JsonUtils,
    ) : BaseViewModel() {

    private val _liveGetMessages = MutableLiveData<List<ReceiveGetMessagesModel>>()
    val liveGetMessages: LiveData<List<ReceiveGetMessagesModel>>
        get() = _liveGetMessages

    private val _liveErrorConvert = MutableLiveData<String>()
    val liveErrorConvert: LiveData<String>
        get() = _liveErrorConvert

    fun getAllMessages() = viewModelIO {
        val requestGetMessages = RequestGetMessages(
            roomId = "",
        )
        repository.emitAndReceive(
            Constance.GET_MESSAGES,
            jsonUtils.toCustomJson(requestGetMessages)
        ) { eventData ->
            val receiveGetMessagesModel =
                jsonUtils.getSafeObjectList<ReceiveGetMessagesModel>(eventData.toString())
            receiveGetMessagesModel.onSuccess {
                _liveGetMessages.postValue(it)
            }.onFailure {
                _liveErrorConvert.postValue(it.message)
            }
        }
    }

    fun sendMessage(
        message: String? = null,
        hashCode: String? = null,
        type: String? = null
    ) {
        val requestSendMessage = if (hashCode != null && type != null) {
            RequestSendMessage(
                roomId = "",
                peerId = "",
                text = message,
            )
        } else {
            RequestSendMessage(
                roomId = "",
                peerId = "",
                text = message
            )
        }
        repository.send(Constance.SEND_MESSAGE, jsonUtils.toCustomJson(requestSendMessage))
    }

}