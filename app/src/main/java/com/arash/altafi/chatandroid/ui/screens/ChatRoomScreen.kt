package com.arash.altafi.chatandroid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.ChatRoomViewModel
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessages
import com.arash.altafi.chatandroid.ui.components.EmptyComponent
import com.arash.altafi.chatandroid.ui.components.LottieComponent
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.utils.ext.fixSummerTime
import com.arash.altafi.chatandroid.utils.ext.getDateClassified
import com.arash.altafi.chatandroid.viewmodel.ProfileViewModel
import saman.zamani.persiandate.PersianDate

@Composable
fun ChatRoomScreen(navController: NavController) {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val chatRoomViewModel: ChatRoomViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()

    val keyboardController = LocalSoftwareKeyboardController.current

    val liveProfile by profileViewModel.liveProfile.observeAsState()

    val liveGetChatRoom by chatRoomViewModel.liveGetChatRoom.collectAsState()
    val liveUserCount by chatRoomViewModel.liveUserCount.collectAsState()
    val liverMessageChatRoom by chatRoomViewModel.liverMessageChatRoom.collectAsState()
    val liveSendChatRoom by chatRoomViewModel.liveSendChatRoom.collectAsState()

    var message by remember { mutableStateOf("") }

    val messages = remember { mutableStateListOf<ReceiveMessages>() }
    var userOnline by remember { mutableIntStateOf(0) }
    var userCount by remember { mutableIntStateOf(0) }

    // Initialize messages list from liveGetChatRoom
    LaunchedEffect(arrayOf(liveGetChatRoom, liveProfile)) {
        if (liveGetChatRoom?.messages != null && liveProfile?.id != null && messages.isEmpty()) {
            messages.clear()
            messages.addAll(liveGetChatRoom!!.messages)
        }
    }

    // Listen for new messages from liverMessageChatRoom
    LaunchedEffect(liverMessageChatRoom) {
        liverMessageChatRoom?.message?.let {
            messages.add(0, it)
        }
    }

    // Listen for change users statues
    LaunchedEffect(arrayOf(liveUserCount, liveGetChatRoom)) {
        userOnline = liveUserCount?.first ?: liveGetChatRoom?.usersOnline ?: 0
        userCount = liveUserCount?.second ?: liveGetChatRoom?.usersCount ?: 0
    }

    // Listen for send new message
    LaunchedEffect(liveSendChatRoom) {
        if (liveSendChatRoom?.message == "ok" && liveSendChatRoom?.data?.text != null) {
            listState.animateScrollToItem(0)
            messages.add(0, liveSendChatRoom!!.data)
        }
    }

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
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_arrow_back_24),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Image(
                        painter = painterResource(R.drawable.icon),
                        contentDescription = context.getString(R.string.app_name),
                        modifier = Modifier.size(50.dp),
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = context.getString(R.string.chat_room),
                        color = Color.White,
                        fontFamily = CustomFont,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp * 0.3f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "کل کاربران:$userCount",
                        color = Color.White,
                        fontFamily = CustomFont,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "آنلاین:$userOnline",
                        color = colorResource(R.color.green_600),
                        fontFamily = CustomFont,
                        fontSize = 12.sp
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
                        val isSendByMe = messages[item].ownerId?.toString() == liveProfile?.id
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
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                        textAlign = TextAlign.Justify,
                                        text = messages[item].text ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black,
                                        fontFamily = CustomFont
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
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                            .weight(1f)
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .padding(bottom = 6.dp)
                                                .fillMaxWidth(0.5f)
                                                .align(Alignment.End),
                                            text = messages[item].fullName ?: "",
                                            maxLines = 1,
                                            textAlign = TextAlign.End,
                                            style = MaterialTheme.typography.bodySmall,
                                            overflow = TextOverflow.Ellipsis,
                                            color = Color.White,
                                            fontFamily = CustomFont
                                        )
                                        Text(
                                            modifier = Modifier
                                                .background(
                                                    brush = Brush.linearGradient(
                                                        colors = listOf(
                                                            colorResource(R.color.blue_600),
                                                            colorResource(R.color.blue_400)
                                                        ),
                                                        start = Offset(0f, 0f), // Top-left
                                                        end = Offset(1000f, 1000f) // Bottom-right
                                                    ),
                                                    shape = RoundedCornerShape(
                                                        topEnd = 0.dp,
                                                        topStart = 8.dp,
                                                        bottomEnd = 4.dp,
                                                        bottomStart = 8.dp,
                                                    )
                                                )
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                                .align(Alignment.End),
                                            textAlign = TextAlign.Justify,
                                            text = messages[item].text ?: "",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White,
                                            fontFamily = CustomFont
                                        )
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
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.Transparent
                                        ),
                                        onClick = {
                                            messages[item].ownerId?.let {
                                                navController.navigate(Route.ProfileUserScreen(it.toString()))
                                            }
                                        }
                                    ) {
                                        AsyncImage(
                                            model = messages[item].avatar,
                                            contentDescription = messages[item].fullName,
                                            modifier = Modifier
                                                .padding(top = 6.dp)
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    0.5.dp,
                                                    Color.Green,
                                                    CircleShape
                                                ),
                                            contentScale = ContentScale.Crop,
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
                                colorResource(R.color.gray_700),
                                colorResource(R.color.gray_900)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(1000f, 1000f)
                        ),
                        shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
                    )
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
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
                                    chatRoomViewModel.sendMessagesChatRoom(
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
                                chatRoomViewModel.sendMessagesChatRoom(
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
}