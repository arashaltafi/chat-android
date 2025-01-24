package com.arash.altafi.chatandroid.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import kotlinx.coroutines.launch
import com.arash.altafi.chatandroid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBottomSheet(
    isShow: Boolean,
    title: Int,
    body: Int? = null,
    checkboxText: Int? = null,
    onCancel: (() -> Unit)? = null,
    onConfirm: (isChecked: Boolean?) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isChecked by remember { mutableStateOf<Boolean?>(null) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (isShow) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss.invoke()
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = context.getString(title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CustomFont,
                    textAlign = TextAlign.Center
                )
                body?.let {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = context.getString(it),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = CustomFont,
                        textAlign = TextAlign.Center
                    )
                }
                checkboxText?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Checkbox(
                            colors = CheckboxDefaults.colors(
                                checkedColor = colorResource(R.color.blue_500),
                                uncheckedColor = colorResource(R.color.gray_500),
                                checkmarkColor = Color.White
                            ),
                            checked = isChecked == true,
                            onCheckedChange = { checked ->
                                isChecked = checked
                            }
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = context.getString(it),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = CustomFont,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.green_500)
                        ),
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismiss.invoke()
                                    onConfirm.invoke(isChecked)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = context.getString(R.string.yes),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = CustomFont,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.width(32.dp))

                    Button(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.red_500)
                        ),
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismiss.invoke()
                                    onCancel?.invoke()
                                }
                            }
                        }
                    ) {
                        Text(
                            text = context.getString(R.string.no),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = CustomFont,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
