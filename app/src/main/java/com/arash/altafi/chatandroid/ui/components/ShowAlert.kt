package com.arash.altafi.chatandroid.ui.components

import android.view.Gravity
import androidx.compose.foundation.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.tapadoo.alerter.Alerter

@Composable
fun ShowAlert(
    showAlert: Boolean,
    title: String,
    duration: Long? = null,
    isShowTop: Boolean? = null,
    isSuccess: Boolean = true,
    btnText: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current

    Alerter(
        isShown = showAlert,
        onChanged = {},
        enableSwipeToDismiss = true,
        backgroundColor = Color.Transparent,
        duration = duration ?: 5000,
        gravity = isShowTop?.let {
            if (it) Gravity.TOP else Gravity.BOTTOM
        } ?: run {
            Gravity.TOP
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = colorResource(
                        if (isSuccess) R.color.green_500 else R.color.red_500
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 8.dp, horizontal = 22.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = if (isSuccess) Icons.Default.Done else Icons.Default.Close,
                contentDescription = context.getString(R.string.app_name),
                tint = colorResource(
                    if (isSuccess) R.color.green_100 else R.color.red_100
                ),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                fontFamily = CustomFont,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Justify,
            )

            Spacer(modifier = Modifier.weight(1f))

            btnText?.let {
                Button(
                    onClick = {
                        onClick?.invoke()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                    ),
                ) {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        fontFamily = CustomFont,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}