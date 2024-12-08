package com.arash.altafi.chatandroid.ui.navigation

import com.arash.altafi.chatandroid.R

data class NavigationDrawerItem(
    val label: String,
    val icon: Int,
    val badgeCount: Int,
    val selected: Boolean,
)

fun bottomNavigationItems(): List<NavigationDrawerItem> {
    return listOf(
        NavigationDrawerItem(
            label = "home",
            icon = R.drawable.ic_launcher_foreground,
            badgeCount = 4,
            selected = true,
        ),
        NavigationDrawerItem(
            label = "search",
            icon = R.drawable.ic_launcher_foreground,
            badgeCount = 0,
            selected = false,
        ),
        NavigationDrawerItem(
            label = "account",
            icon = R.drawable.ic_launcher_foreground,
            badgeCount = 4,
            selected = false,
        ),
        NavigationDrawerItem(
            label = "test",
            icon = R.drawable.ic_launcher_foreground,
            badgeCount = 1,
            selected = false,
        ),
    )
}