package com.arash.altafi.chatandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arash.altafi.chatandroid.data.model.UserInfoModel
import com.arash.altafi.chatandroid.data.model.req.RequestBlock
import com.arash.altafi.chatandroid.data.model.req.RequestGetMessages
import com.arash.altafi.chatandroid.data.model.req.RequestSendEditProfile
import com.arash.altafi.chatandroid.data.model.res.ReceiveBlockList
import com.arash.altafi.chatandroid.data.model.res.ReceiveEditProfile
import com.arash.altafi.chatandroid.data.model.res.ReceiveUserInfo
import com.arash.altafi.chatandroid.data.repository.DataStoreRepository
import com.arash.altafi.chatandroid.data.repository.SocketRepository
import com.arash.altafi.chatandroid.utils.Constance
import com.arash.altafi.chatandroid.utils.JsonUtils
import com.arash.altafi.chatandroid.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: SocketRepository,
    private val dataStoreRepository: DataStoreRepository,
    private var jsonUtils: JsonUtils,
) : BaseViewModel() {

    private val _liveProfile = MutableLiveData<UserInfoModel>()
    val liveProfile: LiveData<UserInfoModel>
        get() = _liveProfile

    private val _liveBlockList = MutableLiveData<ReceiveBlockList?>(null)
    val liveBlockList: LiveData<ReceiveBlockList?>
        get() = _liveBlockList

    private val _liveBlock = MutableStateFlow<Boolean?>(null)
    val liveBlock: StateFlow<Boolean?>
        get() = _liveBlock

    private val _liveBlockPeer = MutableStateFlow<Boolean?>(null)
    val liveBlockPeer: StateFlow<Boolean?>
        get() = _liveBlockPeer

    private val _liveError = MutableLiveData<Boolean>()
    val liveError: LiveData<Boolean>
        get() = _liveError

    private val _liveLoading = MutableLiveData<Boolean>()
    val liveLoading: LiveData<Boolean>
        get() = _liveLoading

    private val _liveUserInfo = MutableStateFlow<ReceiveUserInfo?>(null)
    val liveUserInfo: StateFlow<ReceiveUserInfo?>
        get() = _liveUserInfo

    init {
        getUserInfo()
        receiveBlock()
    }

    fun getUserPeerInfo(peerId: Int) {
        val requestGetMessages = RequestGetMessages(
            peerId = peerId,
        )

        repository.emitAndReceive(
            Constance.USER_INFO,
            jsonUtils.toCustomJson(requestGetMessages)
        ) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<ReceiveUserInfo>(eventData.toString())
            receiveError.onSuccess {
                viewModelScope.launch {
                    _liveUserInfo.emit(it)
                }
            }
        }
    }

    private fun getUserInfo() = callCache(
        cacheCall = dataStoreRepository.getUserInfo(),
        liveResult = _liveProfile,
        liveError = _liveError,
        liveLoading = _liveLoading
    )

    fun setUserInfo(value: UserInfoModel) = callCache(
        cacheCall = dataStoreRepository.setUserInfo(value),
        liveResult = null,
        liveError = _liveError,
        liveLoading = _liveLoading
    )

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
                setUserInfo(
                    UserInfoModel(
                        id = it.id,
                        name = it.name,
                        family = it.family,
                        avatar = it.avatar,
                        token = it.token,
                        phone = it.phone,
                        bio = it.bio
                    )
                )
                CoroutineScope(Dispatchers.Main).launch {
                    delay(100L)
                    getUserInfo()
                }
            }
        }
    }

    fun getBlockList() {
        repository.emitAndReceive(
            Constance.BLOCK_LIST,
        ) { eventData ->
            val receiveUsers =
                jsonUtils.getSafeObject<ReceiveBlockList>(eventData.toString())
            receiveUsers.onSuccess {
                viewModelScope.launch {
                    _liveBlockList.postValue(null)
                    _liveBlockList.postValue(it)
                }
            }
        }
    }

    fun sendBlock(peerId: Int) {
        val requestBlock = RequestBlock(
            peerId = peerId,
        )

        repository.emitAndReceive(
            Constance.BLOCK,
            jsonUtils.toCustomJson(requestBlock)
        ) { eventData ->
            val receiveUsers =
                jsonUtils.getSafeObject<RequestBlock>(eventData.toString())
            receiveUsers.onSuccess {
                _liveBlock.value = !(it.peerId == 0 ||
                        it.peerId.toString() == "" ||
                        it.peerId.toString().isEmpty())
            }
        }
    }

    fun sendUnBlock(peerId: Int) {
        val requestBlock = RequestBlock(
            peerId = peerId,
        )

        repository.emitAndReceive(
            Constance.UNBLOCK,
            jsonUtils.toCustomJson(requestBlock)
        ) { eventData ->
            val receiveUsers =
                jsonUtils.getSafeObject<RequestBlock>(eventData.toString())
            receiveUsers.onSuccess {
                _liveBlock.value = it.peerId == 0 ||
                        it.peerId.toString() == "" ||
                        it.peerId.toString().isEmpty()
            }
        }
    }

    private fun receiveBlock() {
        repository.onReceivedData(Constance.BLOCK_PEER) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<RequestBlock>(eventData.toString())
            receiveError.onSuccess {
                _liveBlockPeer.value = !(it.peerId == 0 ||
                        it.peerId.toString() == "" ||
                        it.peerId.toString().isEmpty())
            }
        }

        repository.onReceivedData(Constance.UNBLOCK_PEER) { eventData ->
            val receiveError =
                jsonUtils.getSafeObject<RequestBlock>(eventData.toString())
            receiveError.onSuccess {
                _liveBlockPeer.value = it.peerId == 0 ||
                        it.peerId.toString() == "" ||
                        it.peerId.toString().isEmpty()
            }
        }
    }
}