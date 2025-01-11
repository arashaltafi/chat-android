package com.arash.altafi.chatandroid.viewmodel

import com.arash.altafi.chatandroid.data.model.res.ReceiveDialog
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
    private val repository: SocketRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    private val _liveGetDialogs = MutableStateFlow<ReceiveDialog?>(null)
    val liveGetDialogs: StateFlow<ReceiveDialog?>
        get() = _liveGetDialogs

    fun getDialogs() {
        repository.emitAndReceive(
            Constance.DIALOGS,
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveDialog>(eventData.toString())
            receiveError.onSuccess {
                _liveGetDialogs.value = it
            }
        }
    }
}