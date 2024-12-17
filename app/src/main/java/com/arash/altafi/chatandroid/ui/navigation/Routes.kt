package com.arash.altafi.chatandroid.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Dialog : Route

    @Serializable
    data object Register : Route

    @Serializable
    data class Verify(val mobile: String) : Route
}