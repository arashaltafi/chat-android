package com.arash.altafi.chatandroid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.components.LottieComponent
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.utils.ext.fixSummerTime
import com.arash.altafi.chatandroid.utils.ext.getDateClassified
import com.arash.altafi.chatandroid.viewmodel.AuthViewModel
import com.arash.altafi.chatandroid.viewmodel.ChatViewModel
import com.arash.altafi.chatandroid.viewmodel.DialogViewModel
import kotlinx.coroutines.delay
import saman.zamani.persiandate.PersianDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DialogScreen(navController: NavController) {
    val context = LocalContext.current

    val authViewModel: AuthViewModel = hiltViewModel()
    val dialogViewModel: DialogViewModel = hiltViewModel()
    val chatViewModel: ChatViewModel = hiltViewModel()

    val introduce by authViewModel.liveIntroduce.collectAsState()
    val liveGetDialogs by dialogViewModel.liveGetDialogs.collectAsState()

    val liveTyping by chatViewModel.liveTyping.collectAsState()

    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            introduce?.state?.let {
                refreshing = true
                dialogViewModel.getDialogs()
            }
        }
    )

    LaunchedEffect(Unit) {
        authViewModel.sendIntroduce()
    }

    LaunchedEffect(introduce) {
        introduce?.state?.let {
            dialogViewModel.getDialogs()
        }
    }

    LaunchedEffect(liveGetDialogs?.timestamp) {
        if (liveGetDialogs != null) {
            delay(500L)
            refreshing = false
        }
    }

    if (liveGetDialogs == null || liveGetDialogs?.data?.dialogs?.isEmpty() == true) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "دیالوگی یافت نشد",
                    fontFamily = CustomFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                )

                LottieComponent(
                    size = DpSize(width = 200.dp, height = 200.dp),
                    loop = true,
                    lottieFile = R.raw.empty_list
                )
            }
        }
    } else {
        liveGetDialogs?.data?.let {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxSize()
                ) {
                    items(it.dialogs.size) { user ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .shadow(4.dp)
                                .border(
                                    1.dp,
                                    colorResource(R.color.gray_800),
                                    RoundedCornerShape(8.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.gray_300)
                            ),
                            onClick = {
                                navController.navigate(Route.Chat(it.dialogs[user].peerId.toString()))
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                    ) {
                                        AsyncImage(
                                            model = it.dialogs[user].avatar,
                                            contentDescription = it.dialogs[user].name,
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                                .shadow(8.dp),
                                            contentScale = ContentScale.Crop,
                                        )
                                        if (it.dialogs[user].isOnline == true) {
                                            Box(
                                                modifier = Modifier
                                                    .zIndex(2f)
                                                    .align(Alignment.BottomStart)
                                                    .offset(8.dp, 2.dp)
                                                    .background(Color.Green, CircleShape)
                                                    .size(8.dp)
                                            )
                                        }
                                    }
                                    Spacer(Modifier.width(22.dp))
                                    Column {
                                        Text(
                                            text = it.dialogs[user].name
                                                ?: context.getString(R.string.app_name),
                                            fontSize = 14.sp,
                                            fontStyle = FontStyle.Normal,
                                            fontFamily = CustomFont,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = it.dialogs[user].lastMessage ?: "",
                                            fontSize = 12.sp,
                                            fontStyle = FontStyle.Normal,
                                            fontFamily = CustomFont,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = Color.Black
                                        )
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                val lastMsgTime = it.dialogs[user].time ?: "نامشخص"
                                val textLastMsgTime =
                                    if (lastMsgTime == "نامشخص") lastMsgTime else {
                                        PersianDate(
                                            lastMsgTime.toLong().fixSummerTime()
                                        ).getDateClassified()
                                    }
                                val textTyping = it.dialogs[user].isTyping ?: ""
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if ((it.dialogs[user].unreadCount ?: 0) > 0) {
                                        val unreadCount = if (it.dialogs[user].unreadCount!! > 99) {
                                            "99+"
                                        } else it.dialogs[user].unreadCount?.toString()
                                        Text(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .background(
                                                    color = colorResource(R.color.green_700),
                                                    shape = CircleShape
                                                ),
                                            textAlign = TextAlign.Center,
                                            text = unreadCount ?: "",
                                            fontSize = 12.sp,
                                            fontStyle = FontStyle.Normal,
                                            fontFamily = CustomFont,
                                            color = Color.White
                                        )
                                    }
                                    if (
                                        textTyping == "" &&
                                        liveTyping == null ||
                                        liveTyping?.peerId != it.dialogs[user].peerId ||
                                        liveTyping?.message == ""
                                    ) {
                                        Text(
                                            text = textLastMsgTime,
                                            fontSize = 12.sp,
                                            fontStyle = FontStyle.Normal,
                                            fontFamily = CustomFont,
                                            color = Color.Black
                                        )
                                    } else {
                                        LottieComponent(
                                            size = DpSize(width = 24.dp, height = 24.dp),
                                            loop = true,
                                            lottieFile = R.raw.typing
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    scale = true,
                    backgroundColor = colorResource(R.color.gray_700),
                    contentColor = Color.White
                )
            }
        }
    }
}