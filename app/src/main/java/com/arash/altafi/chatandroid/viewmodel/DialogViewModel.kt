package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.viewModelScope
import com.arash.altafi.chatandroid.data.model.req.RequestDeleteDialog
import com.arash.altafi.chatandroid.data.model.res.ReceiveDeleteDialog
import com.arash.altafi.chatandroid.data.model.res.ReceiveDialog
import com.arash.altafi.chatandroid.data.model.res.ReceiveUpdateDialog
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
class DialogViewModel @Inject constructor(
    private val repository: SocketRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    data class DialogListWrapper(
        val data: ReceiveDialog?,
        val timestamp: Long = System.currentTimeMillis()
    )

    private val _liveGetDialogs = MutableStateFlow<DialogListWrapper?>(null)
    val liveGetDialogs: StateFlow<DialogListWrapper?>
        get() = _liveGetDialogs

    private val _liveDeleteDialog = MutableStateFlow<ReceiveDeleteDialog?>(null)
    val liveDeleteDialog: StateFlow<ReceiveDeleteDialog?>
        get() = _liveDeleteDialog

    private val _liveClearHistory = MutableStateFlow<ReceiveDeleteDialog?>(null)
    val liveClearHistory: StateFlow<ReceiveDeleteDialog?>
        get() = _liveClearHistory

    private val _liveUpdateDialog = MutableStateFlow<ReceiveUpdateDialog?>(null)
    val liveUpdateDialog: StateFlow<ReceiveUpdateDialog?>
        get() = _liveUpdateDialog

    init {
        receiveUpdateDialog()
        receiveClearHistory()
        receiveDeleteDialog()
    }

    fun getDialogs() {
        repository.emitAndReceive(
            Constance.DIALOGS,
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveDialog>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveGetDialogs.value = DialogListWrapper(it)
                }
            }
        }
    }

    fun sendDeleteDialog(peerId: Int, deleteBoth: Boolean) {
        val requestDeleteDialog = RequestDeleteDialog(
            peerId = peerId,
            deleteBoth = deleteBoth
        )
        repository.send(
            Constance.DELETE_DIALOG,
            jsonUtils.toCustomJson(requestDeleteDialog)
        )
    }

    fun sendClearHistory(peerId: Int, deleteBoth: Boolean) {
        val requestDeleteDialog = RequestDeleteDialog(
            peerId = peerId,
            deleteBoth = deleteBoth
        )
        repository.send(
            Constance.CLEAR_HISTORY,
            jsonUtils.toCustomJson(requestDeleteDialog)
        )
    }

    private fun receiveDeleteDialog() {
        repository.onReceivedData(Constance.DELETE_DIALOG) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveDeleteDialog>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveDeleteDialog.emit(it)
                }
            }
        }
    }

    private fun receiveClearHistory() {
        repository.onReceivedData(Constance.CLEAR_HISTORY) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveDeleteDialog>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveClearHistory.emit(it)
                }
            }
        }
    }

    private fun receiveUpdateDialog() {
        repository.onReceivedData(Constance.UPDATE_DIALOG) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveUpdateDialog>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveUpdateDialog.emit(it)
                }
            }
        }
    }
}