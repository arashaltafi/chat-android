package com.arash.altafi.chatandroid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.ProfileViewModel
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.components.ImageUrl

@Composable
fun ProfileScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val liveProfile by profileViewModel.liveProfile.observeAsState()

    var bio by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var family by remember { mutableStateOf("") }

    LaunchedEffect(liveProfile) {
        bio = liveProfile?.bio ?: ""
        name = liveProfile?.name ?: ""
        family = liveProfile?.family ?: ""
    }

    var isEditMode by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isEditMode) {
        if (isEditMode) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 10.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    color = colorResource(R.color.transparent_black),
                    shape = RoundedCornerShape(bottomEnd = 300.dp, bottomStart = 300.dp)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
            ) {
                ImageUrl(
                    url = if (isEditMode) R.drawable.ic_user else liveProfile?.avatar,
                    modifier = Modifier
                        .zIndex(1f)
                        .size(120.dp)
                        .clip(CircleShape)
                        .shadow(8.dp)
                        .border(
                            1.dp,
                            Color.White,
                            CircleShape
                        )
                )
                IconButton(
                    onClick = {
                        isEditMode = !isEditMode
                    },
                    modifier = Modifier
                        .zIndex(2f)
                        .align(Alignment.TopEnd)
                        .offset(1.dp, 1.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = if (isEditMode) Icons.Filled.Close else Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black
                    )
                }
            }
        }

        if (isEditMode) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .focusRequester(focusRequester),
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    fontFamily = CustomFont,
                ),
                value = name,
                onValueChange = { newValue ->
                    if (newValue.length <= 20) name = newValue
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
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    fontFamily = CustomFont,
                ),
                value = family,
                onValueChange = { newValue ->
                    if (newValue.length <= 30) family = newValue
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

            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    fontFamily = CustomFont,
                ),
                value = bio,
                onValueChange = { newValue ->
                    if (newValue.length <= 200) bio = newValue
                },
                label = {
                    Text(
                        text = context.getString(R.string.bio),
                        fontSize = 12.sp,
                        fontFamily = CustomFont,
                        color = Color.White
                    )
                },
                placeholder = {
                    Text(
                        text = context.getString(R.string.bio),
                        color = Color.White,
                        style = TextStyle(
                            textAlign = TextAlign.End
                        ),
                        fontFamily = CustomFont,
                    )
                },
                singleLine = false,
                enabled = true,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    autoCorrect = false,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()

                    }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.length < 2 || name.length > 20) {
                        Toast.makeText(context, "لطفا نام را بیشتر از 2 کاراکتر و کمتر از 20 کاراکتر وارد نمایید", Toast.LENGTH_SHORT).show()
                    } else if (family.length < 2 || family.length > 30) {
                        Toast.makeText(context, "لطفا نام خانوادگی را بیشتر از 2 کاراکتر و کمتر از 30 کاراکتر وارد نمایید", Toast.LENGTH_SHORT).show()
                    } else if (bio.length < 5 || bio.length > 200) {
                        Toast.makeText(context, "لطفا بیوگرافی را بیشتر از 5 کاراکتر و کمتر از 200 کاراکتر وارد نمایید", Toast.LENGTH_SHORT).show()
                    } else {
                        profileViewModel.sendEditProfile(
                            name = name,
                            family = family,
                            bio = bio
                        )
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        isEditMode = false
                        Toast.makeText(context, "پروفایل شما با موفقیت بروزرسانی شد", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = context.getString(R.string.update),
                    color = Color.Black,
                    fontFamily = CustomFont,
                    fontSize = 16.sp
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp * 0.5f),
                    text = liveProfile?.name + " " + liveProfile?.family,
                    fontFamily = CustomFont,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.White
                )

                Text(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp * 0.5f),
                    text = liveProfile?.phone ?: "",
                    fontFamily = CustomFont,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                textAlign = TextAlign.Start,
                text = context.getString(R.string.bio) + ":",
                fontFamily = CustomFont,
                fontSize = 16.sp,
                color = Color.White
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Justify,
                text = liveProfile?.bio ?: "",
                fontFamily = CustomFont,
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}