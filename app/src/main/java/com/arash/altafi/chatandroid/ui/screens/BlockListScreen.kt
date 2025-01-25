package com.arash.altafi.chatandroid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.ui.components.LottieComponent
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.components.ShowBottomSheet
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.utils.ext.fixSummerTime
import com.arash.altafi.chatandroid.utils.ext.getDateClassified
import com.arash.altafi.chatandroid.viewmodel.ProfileViewModel
import saman.zamani.persiandate.PersianDate

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun BlockListScreen(navController: NavController) {
    val context = LocalContext.current

    var showBottomSheetUnBlock by remember { mutableStateOf(false) }
    var selectedPeerId by remember { mutableIntStateOf(0) }

    val profileViewModel: ProfileViewModel = hiltViewModel()
    val liveBlockList by profileViewModel.liveBlockList.observeAsState()
    val liveBlock by profileViewModel.liveBlock.collectAsState()

    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            profileViewModel.getBlockList()
        }
    )

    LaunchedEffect(Unit) {
        profileViewModel.getBlockList()
    }

    LaunchedEffect(liveBlockList) {
        refreshing = false
    }

    LaunchedEffect(liveBlock) {
        liveBlock?.let {
            Toast.makeText(context, "کاربر مورد نظر با موفقیت رفع مسدود شد", Toast.LENGTH_SHORT)
                .show()
            profileViewModel.getBlockList()
        }
    }

    liveBlockList?.blockList?.let {
        if (it.isNotEmpty()) {
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
                    items(it.size) { user ->
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
                                )
                                .combinedClickable(
                                    onClick = {
                                        it[user].peerId?.let {
                                            navController.navigate(Route.Chat(it.toString()))
                                        }
                                    },
                                    onLongClick = {
                                        it[user].peerId?.let {
                                            selectedPeerId = it
                                            showBottomSheetUnBlock = true
                                        }

                                    }
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.gray_300)
                            ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp, 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = it[user].avatar,
                                        contentDescription = it[user].name,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .shadow(8.dp)
                                            .border(
                                                1.dp,
                                                if (it[user].lastSeen == "آنلاین") Color.Green else Color.Red,
                                                CircleShape
                                            ),
                                        contentScale = ContentScale.Crop,
                                    )
                                    Spacer(Modifier.width(22.dp))
                                    Text(
                                        text = it[user].name ?: "",
                                        fontSize = 16.sp,
                                        fontStyle = FontStyle.Normal,
                                        fontFamily = CustomFont,
                                        color = Color.Black
                                    )
                                }
                                val lastSeen = it[user].lastSeen ?: "نامشخص"
                                Text(
                                    text = if (lastSeen == "آنلاین" || lastSeen == "نامشخص") lastSeen else {
                                        PersianDate(
                                            lastSeen.toLong().fixSummerTime()
                                        ).getDateClassified()
                                    },
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Normal,
                                    fontFamily = CustomFont,
                                    color = Color.Black
                                )
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

            ShowBottomSheet(
                isShow = showBottomSheetUnBlock,
                title = R.string.set_unblock,
                onConfirm = {
                    profileViewModel.sendUnBlock(peerId = selectedPeerId)
                },
                onDismiss = {
                    showBottomSheetUnBlock = false
                }
            )
        } else {
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
                        text = "موردی یافت نشد",
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
        }
    } ?: run {
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
    }
}