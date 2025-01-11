package com.arash.altafi.chatandroid.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.data.model.UserInfoModel
import com.arash.altafi.chatandroid.ui.components.NetworkConnectivityListener
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

    val focusRequester = remember { FocusRequester() }
    var code by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(liveVerify) {
        liveVerify?.message?.let {
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
                popUpTo("verify") { inclusive = true }
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
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .width(280.dp)
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            textAlign = TextAlign.Start,
                            fontFamily = CustomFont,
                        ),
                        value = code,
                        onValueChange = { newValue ->
                            if (newValue.length <= 4) code = newValue
                        },
                        label = {
                            Text(
                                text = context.getString(R.string.otp),
                                fontSize = 12.sp,
                                fontFamily = CustomFont,
                                color = Color.White
                            )
                        },
                        placeholder = {
                            Text(
                                text = "1234",
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth(),
                                style = TextStyle(
                                    textAlign = TextAlign.Start
                                ),
                                fontFamily = CustomFont,
                            )
                        },
                        singleLine = true,
                        enabled = true,
                        visualTransformation = VisualTransformation.None,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.round_mobile_screen_share_24),
                                contentDescription = context.getString(R.string.otp),
                                tint = Color.White
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                            autoCorrect = false,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                authViewModel.sendVerify(
                                    code = code,
                                    phone = mobile
                                )
                            }
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        authViewModel.sendVerify(
                            code = code,
                            phone = mobile
                        )
                    },
                    modifier = Modifier.width(280.dp).imePadding(),
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