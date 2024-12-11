package com.arash.altafi.chatandroid.ui.navigation

import com.arash.altafi.chatandroid.R

data class NavigationDrawerItem(
    val label: Int,
    val icon: Int,
    val route: String,
)

fun navigationDrawerItems(): List<NavigationDrawerItem> {
    return listOf(
        NavigationDrawerItem(
            label = R.string.dialog,
            icon = R.drawable.round_chat_24,
            route = "dialog",
        ),
        NavigationDrawerItem(
            label = R.string.profile,
            icon = R.drawable.round_person_24,
            route = "profile",
        ),
        NavigationDrawerItem(
            label = R.string.chat_room,
            icon = R.drawable.round_mark_unread_chat_alt_24,
            route = "chat_room",
        ),
        NavigationDrawerItem(
            label = R.string.setting,
            icon = R.drawable.round_settings_24,
            route = "setting",
        ),
    )
}