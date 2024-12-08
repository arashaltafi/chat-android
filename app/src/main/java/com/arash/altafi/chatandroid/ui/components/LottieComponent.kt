package com.arash.altafi.chatandroid.ui.components

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.*
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieComponent(
    size: DpSize,
    loop: Boolean,
    lottieFile: Int
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieFile))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        restartOnPlay = loop,
        reverseOnRepeat = true,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0.5f, 0.75f),
        speed = 1f,
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(size),
        alignment = Alignment.Center
    )
}