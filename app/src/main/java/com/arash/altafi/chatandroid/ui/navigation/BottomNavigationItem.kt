package com.arash.altafi.chatandroid.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = "",
    val badgeCount: Int = 0,
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "home",
                icon = Icons.Filled.Home,
                route = Screens.Home.route,
                badgeCount = 4
            ),
            BottomNavigationItem(
                label = "search",
                icon = Icons.Filled.Search,
                route = Screens.Search.route,
                badgeCount = 0
            ),
            BottomNavigationItem(
                label = "profile",
                icon = Icons.Filled.AccountCircle,
                route = Screens.Profile.route,
                badgeCount = 0
            ),
            BottomNavigationItem(
                label = "test",
                icon = Icons.Filled.Lock,
                route = Screens.Test.route,
                badgeCount = 2
            ),
        )
    }
}

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object Search : Screens("search")
    object Profile : Screens("profile")
    object Test : Screens("test")
}