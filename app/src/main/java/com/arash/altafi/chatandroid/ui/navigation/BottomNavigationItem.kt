package com.arash.altafi.chatandroid.ui.navigation

import com.arash.altafi.chatandroid.R

data class BottomNavigationItem(
    val label: Int,
    val icon: Int,
    val route: String,
    val badgeCount: Int,
)

fun bottomNavigationItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            label = R.string.dialog,
            icon = R.drawable.round_chat_24,
            route = "dialog",
            badgeCount = 4
        ),
        BottomNavigationItem(
            label = R.string.chat_room,
            icon = R.drawable.round_mark_unread_chat_alt_24,
            route = "chat_room",
            badgeCount = 0
        ),
        BottomNavigationItem(
            label = R.string.profile,
            icon = R.drawable.round_person_24,
            route = "profile",
            badgeCount = 0
        ),
        BottomNavigationItem(
            label = R.string.setting,
            icon = R.drawable.round_settings_24,
            route = "setting",
            badgeCount = 0
        ),
    )
}