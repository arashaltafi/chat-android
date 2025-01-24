package com.arash.altafi.chatandroid.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.data.model.UserInfoModel
import com.arash.altafi.chatandroid.ui.components.NetworkConnectivityListener
import com.arash.altafi.chatandroid.ui.components.OTPInputTextFields
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.AuthViewModel
import com.arash.altafi.chatandroid.viewmodel.DataStoreViewModel

@Composable
fun VerifyScreen(navController: NavController, mobile: String) {
    if (mobile == "") {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("خطایی رخ داده است")
        }
        return
    }

    val authViewModel: AuthViewModel = hiltViewModel()
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()

    val liveVerify by authViewModel.liveVerify.collectAsState()

    var isConnected by remember { mutableStateOf(true) }

    NetworkConnectivityListener(onConnectionChanged = { connected ->
        isConnected = connected
    })

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val otpValues = remember { mutableStateListOf("", "", "", "") }

    LaunchedEffect(Unit) {
        keyboardController?.show()
    }

    var hasErrorServer by remember { mutableStateOf(false) }

    LaunchedEffect(liveVerify) {
        liveVerify?.message?.let {
            if (it.contains("خوش آمدید").not()) {
                hasErrorServer = true
            }
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        liveVerify?.token?.let {
            dataStoreViewModel.setToken(it)
        }
        liveVerify?.let {
            dataStoreViewModel.setUserInfo(
                UserInfoModel(
                    id = liveVerify?.id,
                    name = liveVerify?.name,
                    family = liveVerify?.family,
                    avatar = liveVerify?.avatar,
                    token = liveVerify?.token,
                    phone = liveVerify?.phone,
                    bio = liveVerify?.bio
                )
            )
            navController.navigate(Route.Dialog) {
                popUpTo(Route.Verify) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!isConnected) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "No internet connection",
                        tint = Color.Red
                    )
                }
                Text(
                    text = context.getString(R.string.app_name),
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    fontFamily = CustomFont,
                    letterSpacing = 2.sp,
                    textDecoration = TextDecoration.None
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OTPInputTextFields(
                    modifier = Modifier
                        .width(280.dp),
                    otpValues = otpValues,
                    otpLength = 4,
                    onOtpInputComplete = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        authViewModel.sendVerify(
                            code = otpValues.joinToString(""),
                            phone = mobile
                        )
                    },
                    onUpdateOtpValuesByIndex = { index, value ->
                        otpValues[index] = value
                    },
                    isError = hasErrorServer
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        authViewModel.sendVerify(
                            code = otpValues.joinToString(""),
                            phone = mobile
                        )
                    },
                    modifier = Modifier
                        .width(280.dp)
                        .imePadding(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = context.getString(R.string.verify),
                        color = Color.Black,
                        fontFamily = CustomFont,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}