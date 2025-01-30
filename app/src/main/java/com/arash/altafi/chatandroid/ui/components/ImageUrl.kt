package com.arash.altafi.chatandroid.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.R

@Composable
fun ImageUrl(
    url: Any? = null,
    alt: String? = null,
    placeholder: Int = R.drawable.ic_user,
    contentScale: ContentScale? = null,
    modifier: Modifier
) {
    AsyncImage(
        model = url,
        placeholder = painterResource(placeholder),
        error = painterResource(placeholder),
        fallback = painterResource(placeholder),
        contentDescription = alt,
        modifier = modifier,
        contentScale = contentScale ?: ContentScale.Crop,
    )
}