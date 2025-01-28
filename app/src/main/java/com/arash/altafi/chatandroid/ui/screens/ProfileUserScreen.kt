package com.arash.altafi.chatandroid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.arash.altafi.chatandroid.ui.components.PopupMenu
import com.arash.altafi.chatandroid.ui.components.PopupMenuItem
import com.arash.altafi.chatandroid.ui.components.ShowBottomSheet
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.utils.ext.borderBottom
import com.arash.altafi.chatandroid.utils.ext.fixSummerTime
import com.arash.altafi.chatandroid.utils.ext.getDateClassified
import com.arash.altafi.chatandroid.viewmodel.DialogViewModel
import com.arash.altafi.chatandroid.viewmodel.ProfileViewModel
import saman.zamani.persiandate.PersianDate
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.viewmodel.ChatViewModel

@Composable
fun ProfileUserScreen(navController: NavController, id: String) {
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

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val profileViewModel: ProfileViewModel = hiltViewModel()
    val dialogViewModel: DialogViewModel = hiltViewModel()
    val chatViewModel: ChatViewModel = hiltViewModel()

    val liveUserInfo by profileViewModel.liveUserInfo.collectAsState()

    val liveBlockPeer by profileViewModel.liveBlockPeer.collectAsState()
    val liveBlock by profileViewModel.liveBlock.collectAsState()

    var isBlockPeer by remember { mutableStateOf(false) }
    var isBlock by remember { mutableStateOf(false) }

    val liveClearHistory by dialogViewModel.liveClearHistory.collectAsState()
    val liveDeleteDialog by dialogViewModel.liveDeleteDialog.collectAsState()

    val liveTyping by chatViewModel.liveTyping.collectAsState()

    var showPopupMenu by remember { mutableStateOf(false) }
    var showBottomSheetDeleteDialog by remember { mutableStateOf(false) }
    var showBottomSheetClearHistory by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        profileViewModel.getUserPeerInfo(id.toInt())
    }

    LaunchedEffect(liveUserInfo) {
        liveUserInfo?.let {
            isBlock = liveUserInfo?.peerInfo?.isBlockByMe == true
            isBlockPeer = liveUserInfo?.peerInfo?.isBlock == true
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

    // handle Clear History
    LaunchedEffect(liveClearHistory) {
        if (liveClearHistory?.peerId == id.toInt()) {
            Toast.makeText(context, "تاریخچه با موفقیت حذف شد", Toast.LENGTH_SHORT).show()
        }
    }

    // handle Delete Dialog
    LaunchedEffect(liveDeleteDialog) {
        if (liveDeleteDialog?.peerId == id.toInt()) {
            Toast.makeText(context, "گفتگو با موفقیت حذف شد", Toast.LENGTH_SHORT).show()
        }
    }

    // show empty screen
    if (liveUserInfo == null) {
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
                    text = "در حال دریافت اطلاعات ...",
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
        return
    }

    // show profile screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, bottom = 10.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .borderBottom(color = Color.White, strokeWidth = 2.dp),
        ) {
            // Background Image
            AsyncImage(
                model = liveUserInfo?.peerInfo?.avatar,
                contentDescription = liveUserInfo?.peerInfo?.name,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(16.dp),
                alpha = 0.5f,
                contentScale = ContentScale.FillBounds
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 12.dp)
                    .zIndex(999f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .alpha(0.8f)
                        .background(colorResource(R.color.gray_500), CircleShape)
                        .border(1.dp, Color.White, CircleShape),
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

                IconButton(
                    modifier = Modifier
                        .alpha(0.8f)
                        .background(colorResource(R.color.gray_500), CircleShape)
                        .border(1.dp, Color.White, CircleShape),
                    onClick = {
                        showPopupMenu = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "more",
                        tint = Color.White
                    )

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

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .offset(y = -(24).dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .height(120.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = liveUserInfo?.peerInfo?.name + " " + liveUserInfo?.peerInfo?.family,
                        fontFamily = CustomFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    val lastSeen = liveUserInfo?.peerInfo?.lastSeen ?: "نامشخص"
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
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
                        fontFamily = CustomFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
                AsyncImage(
                    model = liveUserInfo?.peerInfo?.avatar,
                    contentDescription = liveUserInfo?.peerInfo?.name,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .shadow(8.dp)
                        .border(
                            1.dp,
                            Color.White,
                            CircleShape
                        )
                        .clickable(
                            onClick = {
                                liveUserInfo?.peerInfo?.let {
                                    navController.navigate(Route.ImageScreen(
                                        title = it.name + " " + it.family,
                                        imageUrl = it.avatar ?: ""
                                    ))
                                }
                            }
                        ),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(start = 32.dp)
                .offset(y = (-32).dp)
                .size(64.dp)
                .align(Alignment.Start),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.blue_500)
            ),
            border = BorderStroke(0.5.dp, Color.White),
            onClick = {
                navController.navigate(Route.Chat(id))
            },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.round_chat_24),
                    contentDescription = context.getString(R.string.chat),
                    tint = Color.White
                )
            }
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            textAlign = TextAlign.Start,
            text = context.getString(R.string.phone) + ":",
            fontFamily = CustomFont,
            fontSize = 16.sp,
            color = Color.White
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, start = 8.dp, end = 8.dp),
            textAlign = TextAlign.Start,
            text = liveUserInfo?.peerInfo?.phone ?: "",
            fontFamily = CustomFont,
            fontSize = 16.sp,
            color = Color.White
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, start = 8.dp, end = 8.dp),
            textAlign = TextAlign.Start,
            text = context.getString(R.string.bio) + ":",
            fontFamily = CustomFont,
            fontSize = 16.sp,
            color = Color.White
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, start = 8.dp, end = 8.dp),
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Justify,
            text = liveUserInfo?.peerInfo?.bio ?: "",
            fontFamily = CustomFont,
            fontSize = 12.sp,
            color = Color.White
        )
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