package com.arash.altafi.chatandroid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.data.model.res.ReceiveMessagesPeer
import com.arash.altafi.chatandroid.ui.components.LottieComponent
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.utils.ext.fixSummerTime
import com.arash.altafi.chatandroid.utils.ext.getDateClassified
import com.arash.altafi.chatandroid.viewmodel.ChatViewModel
import com.arash.altafi.chatandroid.viewmodel.ProfileViewModel
import saman.zamani.persiandate.PersianDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(navController: NavController, id: String) {
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

    val listState = rememberLazyListState()
    val context = LocalContext.current
    val chatViewModel: ChatViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()

    val keyboardController = LocalSoftwareKeyboardController.current

    val liveProfile by profileViewModel.liveProfile.observeAsState()

    val liveGetMessages by chatViewModel.liveGetMessages.collectAsState()
    val liveReceiveMessage by chatViewModel.liveReceiveMessage.collectAsState()
    val liveSendMessage by chatViewModel.liveSendMessage.collectAsState()

    var message by remember { mutableStateOf("") }

    val messages = remember { mutableStateListOf<ReceiveMessagesPeer>() }

    LaunchedEffect(id) {
        chatViewModel.getMessages(id.toInt())
    }

    // Initialize messages list from liveGetChatRoom
    LaunchedEffect(arrayOf(liveGetMessages, liveProfile)) {
        if (liveGetMessages?.messages != null && liveProfile?.id != null && messages.isEmpty()) {
            messages.clear()
            messages.addAll(liveGetMessages!!.messages)
        }
    }

    // Listen for new messages from liverMessageChatRoom
    LaunchedEffect(liveReceiveMessage) {
        liveReceiveMessage?.let {
            messages.add(0, it)
        }
    }

    // Listen for send new message
    LaunchedEffect(liveSendMessage) {
        if (liveSendMessage?.message == "ok" && liveSendMessage?.data?.text != null) {
            listState.animateScrollToItem(0)
//            messages.add(0, liveSendMessage!!.data) //todo fix it
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
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        onClick = {
                            navController.navigate(Route.ProfileUserScreen(id))
                        }
                    ) {
                        AsyncImage(
                            model = liveGetMessages?.peerInfo?.avatar,
                            contentDescription = liveGetMessages?.peerInfo?.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .border(
                                    1.dp,
                                    if (liveGetMessages?.peerInfo?.lastSeen == "online") Color.Green else Color.Red,
                                    CircleShape
                                ),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = liveGetMessages?.peerInfo?.name + " " + liveGetMessages?.peerInfo?.family,
                        color = Color.White,
                        fontFamily = CustomFont,
                        fontWeight = FontWeight.Bold
                    )
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
                            Toast.makeText(context, "more", Toast.LENGTH_SHORT).show()
                            // todo show popup menu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "more",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
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
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LottieComponent(
                            size = DpSize(width = 200.dp, height = 200.dp),
                            loop = true,
                            lottieFile = R.raw.empty_list3
                        )

                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = "پیامی یافت نشد ...",
                            fontFamily = CustomFont,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                        )
                    }
                }
            } else {
                // Chat Messages
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    reverseLayout = true,
                    state = listState
                ) {
                    items(messages.size) { item ->
                        val isSendByMe = messages[item].isComing == true
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
                                            .combinedClickable(
                                                onClick = {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "onClick",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                },
                                                onDoubleClick = {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "onDoubleClick",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                }
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
                                                        topEnd = 0.dp,
                                                        topStart = 8.dp,
                                                        bottomEnd = 4.dp,
                                                        bottomStart = 8.dp,
                                                    )
                                                )
                                                .combinedClickable(
                                                    onClick = {
                                                        Toast
                                                            .makeText(
                                                                context,
                                                                "onClick",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                    },
                                                    onDoubleClick = {
                                                        Toast
                                                            .makeText(
                                                                context,
                                                                "onDoubleClick",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                    }
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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
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
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    chatViewModel.sendMessages(
                                        message = message
                                    )
                                    message = ""
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    painter = painterResource(R.drawable.round_send_24),
                                    contentDescription = context.getString(R.string.write_hint),
                                    tint = Color.White
                                )
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
                                chatViewModel.sendMessages(
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