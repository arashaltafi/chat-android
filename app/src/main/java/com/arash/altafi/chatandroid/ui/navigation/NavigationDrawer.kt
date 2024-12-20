package com.arash.altafi.chatandroid.ui.navigation

import com.arash.altafi.chatandroid.R

data class NavigationDrawerItem(
    val label: Int,
    val icon: Int,
    val route: Route,
)

fun navigationDrawerItems(): List<NavigationDrawerItem> {
    return listOf(
        NavigationDrawerItem(
            label = R.string.dialog,
            icon = R.drawable.round_chat_24,
            route = Route.Dialog,
        ),
        NavigationDrawerItem(
            label = R.string.profile,
            icon = R.drawable.round_person_24,
            route = Route.Profile,
        ),
        NavigationDrawerItem(
            label = R.string.chat_room,
            icon = R.drawable.round_mark_unread_chat_alt_24,
            route = Route.ChatRoom,
        ),
        NavigationDrawerItem(
            label = R.string.setting,
            icon = R.drawable.round_settings_24,
            route = Route.Setting,
        ),
    )
}