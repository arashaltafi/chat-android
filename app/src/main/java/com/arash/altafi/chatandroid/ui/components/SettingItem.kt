package com.arash.altafi.chatandroid.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.theme.CustomFont

@Composable
fun SettingItem(
    context: Context,
    title: Int,
    icon: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        onClick.invoke()
                    }
                )
                .padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = context.getString(R.string.app_name),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = context.getString(title),
                    fontFamily = CustomFont,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = context.getString(R.string.app_name),
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            thickness = 1.dp,
            color = Color.White
        )
    }
}