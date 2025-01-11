package com.arash.altafi.chatandroid.ui.screens

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.ChatRoomViewModel
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessages
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
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
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

            // Chat Messages
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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
                                                    topEnd = 4.dp,
                                                    topStart = 8.dp,
                                                    bottomEnd = 0.dp,
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
                                AsyncImage(
                                    model = messages[item].avatar,
                                    contentDescription = "arash",
                                    modifier = Modifier
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

            // BottomBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .width(LocalConfiguration.current.screenWidthDp.dp * 0.1f),
                        onClick = {
                            Toast.makeText(context, "msg: $message", Toast.LENGTH_SHORT).show()
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon),
                            contentDescription = context.getString(R.string.app_name),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .width(LocalConfiguration.current.screenWidthDp.dp * 0.9f),
                        textStyle = TextStyle(
                            textAlign = TextAlign.Start,
                            fontFamily = CustomFont,
                        ),
                        value = message,
                        onValueChange = { newValue ->
                            message = newValue
                        },
                        label = {
                            Text(
                                text = context.getString(R.string.write_hint),
                                fontSize = 12.sp,
                                fontFamily = CustomFont,
                                color = Color.White
                            )
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
                        singleLine = true,
                        enabled = true,
                        visualTransformation = VisualTransformation.None,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            autoCorrect = false
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
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