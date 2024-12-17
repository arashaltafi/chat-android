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
import androidx.compose.ui.focus.FocusDirection
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
import com.arash.altafi.chatandroid.ui.components.NetworkConnectivityListener
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val liveRegister by authViewModel.liveRegister.collectAsState()

    var isConnected by remember { mutableStateOf(true) }

    NetworkConnectivityListener(onConnectionChanged = { connected ->
        isConnected = connected
    })

    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    var mobile by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var family by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(liveRegister) {
        liveRegister?.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            navController.navigate(Route.Verify(mobile))
            authViewModel.resetRegisterState()
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
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .width(280.dp)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = CustomFont,
                    ),
                    value = name,
                    onValueChange = { newValue ->
                        name = newValue
                    },
                    label = {
                        Text(
                            text = context.getString(R.string.name),
                            fontSize = 12.sp,
                            fontFamily = CustomFont,
                            color = Color.White
                        )
                    },
                    placeholder = {
                        Text(
                            text = context.getString(R.string.name),
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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.round_person_24),
                            contentDescription = context.getString(R.string.name),
                            tint = Color.White
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        autoCorrect = false
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    )
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .width(280.dp),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = CustomFont,
                    ),
                    value = family,
                    onValueChange = { newValue ->
                        family = newValue
                    },
                    label = {
                        Text(
                            text = context.getString(R.string.family),
                            fontSize = 12.sp,
                            fontFamily = CustomFont,
                            color = Color.White
                        )
                    },
                    placeholder = {
                        Text(
                            text = context.getString(R.string.family),
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
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.round_person_24),
                            contentDescription = context.getString(R.string.family),
                            tint = Color.White
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        autoCorrect = false
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    )
                )

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .width(280.dp),
                        textStyle = TextStyle(
                            textAlign = TextAlign.Start,
                            fontFamily = CustomFont,
                        ),
                        value = mobile,
                        onValueChange = { newValue ->
                            mobile = newValue
                        },
                        label = {
                            Text(
                                text = context.getString(R.string.phone),
                                fontSize = 12.sp,
                                fontFamily = CustomFont,
                                color = Color.White
                            )
                        },
                        placeholder = {
                            Text(
                                text = "09",
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
                                painter = painterResource(R.drawable.round_phone_android_24),
                                contentDescription = context.getString(R.string.phone),
                                tint = Color.White
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done,
                            autoCorrect = false,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                authViewModel.sendRegister(
                                    name = name,
                                    family = family,
                                    phone = mobile
                                )
                            }
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.width(280.dp)
                ) {
                    Text(
                        text = "قبلا ثبت نام کرده اید؟",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Normal,
                        fontFamily = CustomFont,
                        letterSpacing = 2.sp,
                        textDecoration = TextDecoration.None
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text(
                            text = context.getString(R.string.login),
                            fontSize = 14.sp,
                            color = Color.Blue,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                            fontFamily = CustomFont,
                            letterSpacing = 2.sp,
                            textDecoration = TextDecoration.Underline,
                        )
                    }
                }


                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        authViewModel.sendRegister(
                            name = name,
                            family = family,
                            phone = mobile
                        )
                    },
                    modifier = Modifier.width(280.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = context.getString(R.string.register),
                        color = Color.Black,
                        fontFamily = CustomFont,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}