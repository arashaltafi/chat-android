package com.arash.altafi.chatandroid.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    val route: String

    @Serializable
    data object Splash : Route {
        override val route: String = ".ui.navigation.Route.Splash"
    }

    @Serializable
    data object Login : Route {
        override val route: String = ".ui.navigation.Route.Login"
    }

    @Serializable
    data object Dialog : Route {
        override val route: String = ".ui.navigation.Route.Dialog"
    }

    @Serializable
    data object Users : Route {
        override val route: String = ".ui.navigation.Route.Users"
    }

    @Serializable
    data object BlockList : Route {
        override val route: String = ".ui.navigation.Route.BlockList"
    }

    @Serializable
    data object About : Route {
        override val route: String = ".ui.navigation.Route.About"
    }

    @Serializable
    data class Chat(val id: String? = null) : Route {
        override val route: String = ".ui.navigation.Route.Chat"
    }

    @Serializable
    data object Register : Route {
        override val route: String = ".ui.navigation.Route.Register"
    }

    @Serializable
    data object Profile : Route {
        override val route: String = ".ui.navigation.Route.Profile"
    }

    @Serializable
    data class ProfileUserScreen(val id: String? = null) : Route {
        override val route: String = ".ui.navigation.Route.ProfileUserScreen"
    }

    @Serializable
    data object ChatRoom : Route {
        override val route: String = ".ui.navigation.Route.ChatRoom"
    }

    @Serializable
    data object Setting : Route {
        override val route: String = ".ui.navigation.Route.Setting"
    }

    @Serializable
    data class Verify(val mobile: String) : Route {
        override val route: String = ".ui.navigation.Route.Verify"
    }

    @Serializable
    data class ImageScreen(var title: String, val imageUrl: String) : Route {
        override val route: String = ".ui.navigation.Route.ImageScreen"
    }
}