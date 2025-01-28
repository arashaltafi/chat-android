package com.arash.altafi.chatandroid.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.theme.CustomFont

@Composable
fun ShowAlertDialog(
    title: Int,
    body: Int? = null,
    checkboxText: Int? = null,
    icon: ImageVector = Icons.Default.Info,
    onCancel: () -> Unit,
    onConfirm: (isChecked: Boolean?) -> Unit,
) {
    val context = LocalContext.current
    var isChecked by remember { mutableStateOf<Boolean?>(null) }

    AlertDialog(
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = icon,
                contentDescription = "Icon",
                tint = Color.White
            )
        },

        title = {
            Text(
                text = context.getString(title),
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                fontFamily = CustomFont,
                color = Color.White
            )
        },
        text = {
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
        },
        onDismissRequest = {
            onCancel.invoke()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm.invoke(isChecked)
                }
            ) {
                Text(
                    text = "بله",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Normal,
                    fontFamily = CustomFont,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onCancel.invoke()
                }
            ) {
                Text(
                    text = "خیر",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Normal,
                    fontFamily = CustomFont,
                    color = Color.White
                )
            }
        }
    )
}