package com.arash.altafi.chatandroid.viewmodel

import com.arash.altafi.chatandroid.data.model.req.RequestSendEditProfile
import com.arash.altafi.chatandroid.data.model.res.ReceiveEditProfile
import com.arash.altafi.chatandroid.data.repository.DataStoreRepository
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: SocketRepository,
    private val dataStoreRepository: DataStoreRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    private val _liveEditProfile = MutableStateFlow<ReceiveEditProfile?>(null)
    val liveEditProfile: StateFlow<ReceiveEditProfile?>
        get() = _liveEditProfile

    fun sendEditProfile(name: String, family: String, bio: String) {
        val requestSendEditProfile = RequestSendEditProfile(
            token = dataStoreRepository.getTokenString(),
            name = name,
            family = family,
            bio = bio,
        )

        repository.emitAndReceive(
            Constance.EDIT_PROFILE,
            jsonUtils.toCustomJson(requestSendEditProfile)
        ) { eventData ->
            val receiveUsers =
                jsonUtils.getSafeObject<ReceiveEditProfile>(eventData.toString())
            receiveUsers.onSuccess {
                _liveEditProfile.value = it
            }
        }
    }
}