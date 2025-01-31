package com.arash.altafi.chatandroid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessagesPeer
import com.arash.altafi.chatandroid.ui.components.EmptyComponent
import com.arash.altafi.chatandroid.ui.components.ImageUrl
import com.arash.altafi.chatandroid.ui.components.LoadingComponent
import com.arash.altafi.chatandroid.ui.components.PopupMenu
import com.arash.altafi.chatandroid.ui.components.PopupMenuItem
import com.arash.altafi.chatandroid.ui.components.ShowBottomSheet
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.utils.ext.fixSummerTime
import com.arash.altafi.chatandroid.utils.ext.getDateClassified
import com.arash.altafi.chatandroid.viewmodel.ChatViewModel
import com.arash.altafi.chatandroid.viewmodel.DialogViewModel
import com.arash.altafi.chatandroid.viewmodel.ProfileViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(navController: NavController? = null, id: String) {
    if (id == "") {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("خطایی رخ داده است")
        }
        return
    }

    val coroutineScope = rememberCoroutineScope()
    var typingJob: Job? by remember { mutableStateOf(null) }
    var isTyping by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val context = LocalContext.current
    val chatViewModel: ChatViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val dialogViewModel: DialogViewModel = hiltViewModel()

    val liveTyping by chatViewModel.liveTyping.collectAsState()

    val liveClearHistory by dialogViewModel.liveClearHistory.collectAsState()
    val liveDeleteDialog by dialogViewModel.liveDeleteDialog.collectAsState()

    val liveSeenMessage by chatViewModel.liveSeenMessage.collectAsState()
    val liveDeliverMessage by chatViewModel.liveDeliverMessage.collectAsState()
    val liveReactionMessage by chatViewModel.liveReactionMessage.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    val liveProfile by profileViewModel.liveProfile.observeAsState()

    val liveGetMessages by chatViewModel.liveGetMessages.collectAsState()
    val liveReceiveMessage by chatViewModel.liveReceiveMessage.collectAsState()
    val liveReceiveDeleteMessage by chatViewModel.liveReceiveDeleteMessage.collectAsState()

    val liveBlockPeer by profileViewModel.liveBlockPeer.collectAsState()
    val liveBlock by profileViewModel.liveBlock.collectAsState()

    var isBlockPeer by remember { mutableStateOf(false) }
    var isBlock by remember { mutableStateOf(false) }

    var showBottomSheetDeleteDialog by remember { mutableStateOf(false) }
    var showBottomSheetClearHistory by remember { mutableStateOf(false) }
    var showPopupMenu by remember { mutableStateOf(false) }

    LaunchedEffect(liveGetMessages) {
        liveGetMessages?.let {
            isBlock = liveGetMessages?.peerInfo?.isBlockByMe == true
            isBlockPeer = liveGetMessages?.peerInfo?.isBlock == true
        }
    }

    LaunchedEffect(arrayOf(liveBlockPeer, liveBlock)) {
        liveBlockPeer?.let {
            isBlockPeer = liveBlockPeer == true
        }
        liveBlock?.let {
            isBlock = liveBlock == true
        }
    }

    var message by remember { mutableStateOf("") }

    val messages = remember { mutableStateListOf<ReceiveMessagesPeer>() }

    // handle Clear History
    LaunchedEffect(liveClearHistory) {
        if (liveClearHistory?.peerId == id.toInt()) {
            messages.clear()
        }
    }

    // handle Delete Dialog
    LaunchedEffect(liveDeleteDialog) {
        if (liveDeleteDialog?.peerId == id.toInt()) {
            navController?.navigateUp()
        }
    }

    // handle Seen Message
    LaunchedEffect(liveSeenMessage) {
        if (liveSeenMessage?.peerId == id.toInt()) {
            // Update each message's seenTime
//            messages.forEach { it.seenTime = System.currentTimeMillis() }

            // If you need to clear and re-add the updated messages
            val updatedMessages = messages.map {
                it.copy(seenTime = System.currentTimeMillis()) // Assuming ReceiveMessagesPeer is a data class
            }
            messages.clear() // Clear the current list
            messages.addAll(updatedMessages) // Add the updated messages back
        }
    }

    // handle Deliver Message
    LaunchedEffect(liveDeliverMessage) {
        if (liveDeliverMessage?.peerId == id.toInt()) {
            // Update each message's seenTime
//            messages.forEach { it.deliverTime = System.currentTimeMillis() }

            // If you need to clear and re-add the updated messages
            val updatedMessages = messages.map {
                it.copy(deliverTime = System.currentTimeMillis()) // Assuming ReceiveMessagesPeer is a data class
            }
            messages.clear() // Clear the current list
            messages.addAll(updatedMessages) // Add the updated messages back
        }
    }

    // handle Reaction Message
    LaunchedEffect(liveReactionMessage) {
        if (liveReactionMessage?.peerId == id.toInt()) {
            val updatedMessages = messages.map {
                if (it.id == liveReactionMessage?.messageId) {
                    it.copy(reaction = liveReactionMessage?.reaction)
                } else {
                    it
                }
            }
            messages.clear() // Clear the current list
            messages.addAll(updatedMessages) // Add the updated messages back
        }
    }

    // handle Delete Message
    LaunchedEffect(liveReceiveDeleteMessage) {
        if (liveReceiveDeleteMessage?.peerId == id.toInt()) {
            val updatedMessages = messages.filter {
                it.id != liveReceiveDeleteMessage?.messageId
            }
            messages.clear() // Clear the current list
            messages.addAll(updatedMessages) // Add the updated messages back
        }
    }

    LaunchedEffect(id) {
        chatViewModel.getMessages(id.toInt())
        chatViewModel.sendSeenMessage(id.toInt())
    }

    // Initialize messages list from liveGetMessages
    LaunchedEffect(arrayOf(liveGetMessages, liveProfile)) {
        if (
            liveGetMessages?.messages != null &&
            (liveGetMessages?.messages?.size ?: 0) > 0 &&
            liveProfile?.id != null &&
            messages.isEmpty() &&
            liveClearHistory?.peerId == null
        ) {
            messages.clear()
            messages.addAll(liveGetMessages!!.messages)
        }
    }

    // Listen for new messages from liveReceiveMessage
    LaunchedEffect(liveReceiveMessage) {
        liveReceiveMessage?.let {
            if (it.peerId == id.toInt()) {
                messages.add(0, it.messages)
                if (it.sendedSuccess == true) {
                    listState.animateScrollToItem(0)
                } else {
                    chatViewModel.sendSeenMessage(id.toInt())
                }
            }
        }
    }

    // show empty screen
    if (liveGetMessages == null) {
        LoadingComponent()
        return
    }

    // show chat screen
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.chat_bg_dark),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TopBar
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(colorResource(R.color.blue_300))
                    .padding(horizontal = 16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(
                        modifier = Modifier
                            .fillMaxHeight(),
                        onClick = {
                            navController?.navigateUp()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_arrow_back_24),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        onClick = {
                            navController?.navigate(Route.ProfileUserScreen(id))
                        }
                    ) {
                        ImageUrl(
                            url = liveGetMessages?.peerInfo?.avatar,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(
                                    1.dp,
                                    if (liveGetMessages?.peerInfo?.lastSeen == "آنلاین") Color.Green else Color.Red,
                                    CircleShape
                                )
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = liveGetMessages?.peerInfo?.name + " " + liveGetMessages?.peerInfo?.family,
                            color = Color.White,
                            fontFamily = CustomFont,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val lastSeen = liveGetMessages?.peerInfo?.lastSeen ?: "نامشخص"
                        Text(
                            modifier = Modifier.padding(top = 2.dp),
                            text = if (isBlockPeer) {
                                "توسط کاربر مسدود شده اید"
                            } else if (liveTyping?.peerId == id.toInt() && liveTyping?.message != "") {
                                liveTyping?.message ?: ""
                            } else {
                                if (lastSeen == "آنلاین" || lastSeen == "نامشخص") lastSeen else {
                                    PersianDate(
                                        lastSeen.toLong().fixSummerTime()
                                    ).getDateClassified()
                                }
                            },
                            color = Color.White,
                            fontFamily = CustomFont,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(LocalConfiguration.current.screenWidthDp.dp * 0.3f),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        modifier = Modifier
                            .fillMaxHeight(),
                        onClick = {
                            showPopupMenu = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "more",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }

                    PopupMenu(
                        showMenu = showPopupMenu,
                        onHideMenu = { showPopupMenu = false },
                        menuItems = listOf(
                            PopupMenuItem(label = if (isBlock) "رفع مسدودی" else "مسدود کردن") {
                                if (isBlock) {
                                    profileViewModel.sendUnBlock(peerId = id.toInt())
                                } else {
                                    profileViewModel.sendBlock(peerId = id.toInt())
                                }
                                showPopupMenu = false
                            },
                            PopupMenuItem(label = "بی صدا کردن") {
                                Toast.makeText(context, "Mute clicked", Toast.LENGTH_SHORT).show()
                                showPopupMenu = false
                            },
                            PopupMenuItem(label = "حذف تاریخچه") {
                                showBottomSheetClearHistory = true
                                showPopupMenu = false
                            },
                            PopupMenuItem(label = "حذف گفتگو") {
                                showBottomSheetDeleteDialog = true
                                showPopupMenu = false
                            },
                        )
                    )
                }
            }

            // empty ui
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    EmptyComponent(
                        title = "پیامی یافت نشد!"
                    )
                }
            } else {
                // Chat Messages
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 12.dp, bottom = 16.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    reverseLayout = true,
                    state = listState
                ) {
                    items(messages.size) { item ->
                        val isSendByMe = messages[item].isComing == false
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = if (isSendByMe) Alignment.CenterStart else Alignment.CenterEnd
                        ) {
                            if (isSendByMe) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                ) {
                                    Box {
                                        Text(
                                            modifier = Modifier
                                                .background(
                                                    brush = Brush.linearGradient(
                                                        colors = listOf(
                                                            colorResource(R.color.gray_200),
                                                            colorResource(R.color.gray_300)
                                                        ),
                                                        start = Offset(0f, 0f), // Top-left
                                                        end = Offset(1000f, 1000f) // Bottom-right
                                                    ),
                                                    shape = RoundedCornerShape(
                                                        topEnd = 8.dp,
                                                        topStart = 4.dp,
                                                        bottomEnd = 8.dp,
                                                        bottomStart = 0.dp,
                                                    )
                                                )
                                                .combinedClickable(
                                                    onClick = {},
                                                    onDoubleClick = {
                                                        messages[item].id?.let {
                                                            chatViewModel.sendReactionMessage(
                                                                peerId = id.toInt(),
                                                                messageId = it,
                                                                reaction = "❤"
                                                            )
                                                        }
                                                    }
                                                )
                                                .padding(horizontal = 16.dp, vertical = 8.dp),
                                            textAlign = TextAlign.Justify,
                                            text = messages[item].text ?: "",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.Black,
                                            fontFamily = CustomFont
                                        )
                                        if (messages[item].reaction != "") {
                                            Text(
                                                modifier = Modifier
                                                    .zIndex(2f)
                                                    .align(Alignment.BottomEnd)
                                                    .offset(8.dp, 8.dp),
                                                textAlign = TextAlign.Justify,
                                                text = messages[item].reaction ?: "",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Black,
                                                fontFamily = CustomFont
                                            )
                                        }
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val seenState = if (messages[item].seenTime != 0L) {
                                            Pair(Icons.Default.DoneAll, R.color.blue_500)
                                        } else if (messages[item].deliverTime != 0L) {
                                            Pair(Icons.Default.DoneAll, R.color.white)
                                        } else {
                                            Pair(Icons.Default.Done, R.color.white)
                                        }
                                        Icon(
                                            modifier = Modifier.size(16.dp),
                                            imageVector = seenState.first,
                                            contentDescription = context.getString(R.string.app_name),
                                            tint = colorResource(seenState.second)
                                        )

                                        Spacer(
                                            modifier = Modifier.width(16.dp)
                                        )

                                        Text(
                                            modifier = Modifier.padding(top = 6.dp),
                                            text = PersianDate(
                                                messages[item].sendTime?.toLong()
                                                    ?.fixSummerTime()
                                            ).getDateClassified(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White,
                                            fontFamily = CustomFont
                                        )
                                    }

                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                            .weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.End)
                                        ) {
                                            Text(
                                                modifier = Modifier
                                                    .background(
                                                        brush = Brush.linearGradient(
                                                            colors = listOf(
                                                                colorResource(R.color.blue_600),
                                                                colorResource(R.color.blue_400)
                                                            ),
                                                            start = Offset(0f, 0f), // Top-left
                                                            end = Offset(
                                                                1000f,
                                                                1000f
                                                            ) // Bottom-right
                                                        ),
                                                        shape = RoundedCornerShape(
                                                            topEnd = 0.dp,
                                                            topStart = 8.dp,
                                                            bottomEnd = 4.dp,
                                                            bottomStart = 8.dp,
                                                        )
                                                    )
                                                    .combinedClickable(
                                                        onClick = {},
                                                        onDoubleClick = {
                                                            messages[item].id?.let {
                                                                chatViewModel.sendReactionMessage(
                                                                    peerId = id.toInt(),
                                                                    messageId = it,
                                                                    reaction = "❤"
                                                                )
                                                            }
                                                        }
                                                    )
                                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                                textAlign = TextAlign.Justify,
                                                text = messages[item].text ?: "",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.White,
                                                fontFamily = CustomFont
                                            )
                                            if (messages[item].reaction != "") {
                                                Text(
                                                    modifier = Modifier
                                                        .zIndex(2f)
                                                        .align(Alignment.BottomStart)
                                                        .offset(-(8).dp, 8.dp),
                                                    textAlign = TextAlign.Justify,
                                                    text = messages[item].reaction ?: "",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.Black,
                                                    fontFamily = CustomFont
                                                )
                                            }
                                        }
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 6.dp)
                                                .align(Alignment.End),
                                            text = PersianDate(
                                                messages[item].sendTime?.fixSummerTime()
                                            ).getDateClassified(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White,
                                            fontFamily = CustomFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // BottomBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                colorResource(
                                    if (isBlock) R.color.red_700 else R.color.gray_700
                                ),
                                colorResource(
                                    if (isBlock) R.color.red_900 else R.color.gray_900
                                )
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(1000f, 1000f)
                        ),
                        shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
                    )
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (isBlock) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    profileViewModel.sendUnBlock(peerId = id.toInt())
                                }
                            )
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center,
                        text = "رفع مسدودی",
                        color = Color.White,
                        fontFamily = CustomFont,
                        fontSize = 16.sp,
                    )
                } else {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            textAlign = TextAlign.Start,
                            fontFamily = CustomFont,
                        ),
                        value = message,
                        onValueChange = { newValue ->
                            message = newValue

                            // Cancel any existing debounce job
                            typingJob?.cancel()

                            if (!isTyping) {
                                chatViewModel.sendStartTyping(id.toInt())
                                isTyping = true
                            }

                            // Start a new debounce job
                            typingJob = coroutineScope.launch {
                                delay(5000) // Wait for 5 seconds
                                chatViewModel.sendStopTyping(id.toInt())
                                isTyping = false
                            }
                        },
                        placeholder = {
                            Text(
                                text = context.getString(R.string.write_hint),
                                color = Color.White,
                                style = TextStyle(
                                    textAlign = TextAlign.End
                                ),
                                fontFamily = CustomFont,
                            )
                        },
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    if (message.trim() == "" || message.trim().isEmpty()) {
                                        Toast.makeText(
                                            context,
                                            "لطفا ابتدا متن پیام را وارد نمایید",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@IconButton
                                    }
                                    chatViewModel.sendMessage(
                                        peerId = id.toInt(),
                                        message = message
                                    )
                                    message = ""
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    painter = painterResource(R.drawable.round_send_24),
                                    contentDescription = context.getString(R.string.send),
                                    tint = Color.White
                                )
                            }
                        },
                        trailingIcon = {
                            Row {
                                IconButton(
                                    onClick = {
                                        //todo fixme open gallery
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = Icons.Default.Attachment,
                                        contentDescription = context.getString(R.string.attachment),
                                        tint = Color.White
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        //todo fixme open emoji box
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = Icons.Default.EmojiEmotions,
                                        contentDescription = context.getString(R.string.emoji),
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        enabled = true,
                        visualTransformation = VisualTransformation.None,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Send,
                            autoCorrect = false
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (message.trim() == "" || message.trim().isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "لطفا ابتدا متن پیام را وارد نمایید",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@KeyboardActions
                                }
                                chatViewModel.sendMessage(
                                    peerId = id.toInt(),
                                    message = message
                                )
                                keyboardController?.hide()
                                message = ""
                            }
                        )
                    )
                }
            }
        }
    }

    // bottom sheet dialogs
    ShowBottomSheet(
        isShow = showBottomSheetDeleteDialog,
        title = R.string.title_delete_dialog,
        checkboxText = R.string.delete_both,
        onConfirm = { isChecked ->
            dialogViewModel.sendDeleteDialog(id.toInt(), isChecked == true)
        },
        onDismiss = {
            showBottomSheetDeleteDialog = false
        }
    )

    ShowBottomSheet(
        isShow = showBottomSheetClearHistory,
        title = R.string.title_clear_history,
        checkboxText = R.string.delete_both,
        onConfirm = { isChecked ->
            dialogViewModel.sendClearHistory(id.toInt(), isChecked == true)
        },
        onDismiss = {
            showBottomSheetClearHistory = false
        }
    )
}