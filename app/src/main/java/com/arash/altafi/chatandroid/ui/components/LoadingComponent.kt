package com.arash.altafi.chatandroid.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.theme.CustomFont

@Composable
fun LoadingComponent(
    title: String = "در حال دریافت اطلاعات ..."
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(56.dp),
                color = Color.White,
                trackColor = Color.Gray,
            )

            Spacer(Modifier.height(64.dp))

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = title,
                fontFamily = CustomFont,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
            )

            Spacer(Modifier.height(32.dp))

            LottieComponent(
                size = DpSize(width = 200.dp, height = 200.dp),
                loop = true,
                lottieFile = R.raw.empty_list
            )
        }
    }
}