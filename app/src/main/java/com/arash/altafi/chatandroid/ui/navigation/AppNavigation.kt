package com.arash.altafi.chatandroid.ui.navigation

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.ui.theme.ChatAndroidTheme
import com.arash.altafi.chatandroid.viewmodel.DataStoreViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.components.BackPressHandler
import com.arash.altafi.chatandroid.ui.components.NetworkConnectivityListener
import com.arash.altafi.chatandroid.ui.screens.ChatRoomScreen
import com.arash.altafi.chatandroid.ui.screens.ChatScreen
import com.arash.altafi.chatandroid.ui.screens.DialogScreen
import com.arash.altafi.chatandroid.ui.screens.LoginScreen
import com.arash.altafi.chatandroid.ui.screens.ProfileScreen
import com.arash.altafi.chatandroid.ui.screens.ProfileUserScreen
import com.arash.altafi.chatandroid.ui.screens.RegisterScreen
import com.arash.altafi.chatandroid.ui.screens.SettingScreen
import com.arash.altafi.chatandroid.ui.screens.SplashScreen
import com.arash.altafi.chatandroid.ui.screens.UsersScreen
import com.arash.altafi.chatandroid.ui.screens.VerifyScreen
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.AuthViewModel
import com.arash.altafi.chatandroid.viewmodel.MainViewModel

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val packageName = context.packageName

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navController = rememberNavController()
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    val liveErrorConvert by mainViewModel.liveErrorConvert.collectAsState()
    val liveError by mainViewModel.liveError.collectAsState()
    val liveUnAuthorized by mainViewModel.liveUnAuthorized.collectAsState()
    val liveLogout by authViewModel.liveLogout.collectAsState()

    val userInfo by dataStoreViewModel.cachedUserInfo.observeAsState()

    LaunchedEffect(Unit) {
        mainViewModel.receiveError()
        mainViewModel.receiveUnAuthorized()
    }

    LaunchedEffect(liveLogout) {
        liveLogout?.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            dataStoreViewModel.clearAll()
            drawerState.close()
            navController.navigate(Route.Login)
            authViewModel.resetLogoutState()
        }
    }

    LaunchedEffect(liveErrorConvert) {
        liveErrorConvert?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(liveError) {
        liveError?.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        mainViewModel.resetErrorState()
    }

    LaunchedEffect(liveUnAuthorized) {
        liveUnAuthorized?.message?.let {
            dataStoreViewModel.setToken("")
            dataStoreViewModel.clearUserInfo()
            navController.navigate(Route.Login) {
                popUpTo("dialog") { inclusive = true }
            }
        }
        mainViewModel.resetUnAuthorizedState()
    }

    var isConnected by remember { mutableStateOf(false) }

    NetworkConnectivityListener(onConnectionChanged = { connected ->
        isConnected = connected
    })

    LaunchedEffect(Unit) {
        dataStoreViewModel.getTheme()
    }

    val fabVisible by remember { mutableStateOf(true) }

    val darkTheme: Boolean = isSystemInDarkTheme()
    val theme by dataStoreViewModel.cachedTheme.observeAsState()

    LaunchedEffect(theme) {
        val isDark = if (darkTheme) "dark" else "light"
        if (theme == "") {
            dataStoreViewModel.setTheme(isDark)
        }
    }

    var doubleBackToExitPressedOnce by remember { mutableStateOf(false) }
    var navigationSelectedItem by remember { mutableIntStateOf(0) }
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route
    val isSplashScreen = currentDestination == context.packageName + Route.Splash.route
    val isDialogScreen = currentDestination == context.packageName + Route.Dialog.route
    val allowBottomBar = arrayOf(
        packageName + Route.Dialog.route,
//        packageName + Route.ChatRoom.route,
        packageName + Route.Profile.route,
        packageName + Route.Setting.route
    )
    val allowTopBar = arrayOf(
        packageName + Route.Users.route,
        packageName + Route.Dialog.route,
//        packageName + Route.ChatRoom.route,
        packageName + Route.Profile.route,
        packageName + Route.Setting.route
    )
    val allowNavigationBar = arrayOf(
        packageName + Route.Users.route,
        packageName + Route.Dialog.route,
//        packageName + Route.ChatRoom.route,
        packageName + Route.Profile.route,
        packageName + Route.Setting.route
    )

    var isScrolled by remember { mutableStateOf(false) }

    ChatAndroidTheme(
        darkTheme = theme == "dark"
    ) {
        ModalNavigationDrawer(
            gesturesEnabled = currentDestination in allowNavigationBar,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(LocalConfiguration.current.screenWidthDp.dp * 0.7f),
                    drawerContainerColor = MaterialTheme.colorScheme.primary,
                    drawerContentColor = MaterialTheme.colorScheme.primary,
                    drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                model = userInfo?.avatar,
                                contentDescription = userInfo?.name,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .shadow(8.dp)
                                    .border(
                                        1.dp,
                                        Color.Green,
                                        CircleShape
                                    ),
                                contentScale = ContentScale.Crop,
                            )
                            Spacer(Modifier.width(22.dp))
                            Text(
                                text = userInfo?.name + " " + userInfo?.family,
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Normal,
                                fontFamily = CustomFont,
                                color = Color.White
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            navigationDrawerItems().forEachIndexed { index, item ->
                                val isSelected = index == selectedItemIndex
                                val contentColor = if (isSelected) Color.Magenta else Color.White

                                NavigationDrawerItem(
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                                    label = {
                                        Text(
                                            text = context.getString(item.label),
                                            color = contentColor,
                                            fontFamily = CustomFont
                                        )
                                    },
                                    selected = isSelected,
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = item.icon),
                                            contentDescription = context.getString(item.label),
                                            tint = contentColor
                                        )
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = Color.Transparent,
                                        unselectedContainerColor = Color.Transparent
                                    ),
                                    onClick = {
                                        navController.navigate(item.route)
                                        selectedItemIndex = index
                                        coroutineScope.launch {
                                            drawerState.close()
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp, 0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    authViewModel.sendLogout()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text(
                                    text = context.getString(R.string.logout),
                                    color = Color.White,
                                    fontFamily = CustomFont,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            },
            drawerState = drawerState,
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                topBar = {
                    if (currentDestination in allowTopBar) {
                        @OptIn(ExperimentalMaterial3Api::class)
                        TopAppBar(
                            modifier = Modifier
                                .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)),
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (!isConnected) {
                                        Icon(
                                            imageVector = Icons.Filled.Error,
                                            contentDescription = "No internet connection",
                                            tint = Color.Red
                                        )
                                    }
                                    Text(
                                        text = context.getString(R.string.app_name),
                                        color = Color.White,
                                        fontFamily = CustomFont
                                    )
                                }
                            },
                            navigationIcon = {
                                Row {
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Menu",
                                            tint = Color.White
                                        )
                                    }
                                }
                            },
                            actions = {
                                Row {
                                    IconButton(
                                        onClick = {
                                            dataStoreViewModel.changeTheme()
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = if (theme == "dark") R.drawable.round_light_mode_24 else R.drawable.round_dark_mode_24),
                                            contentDescription = if (theme == "dark") "Switch to Light Theme" else "Switch to Dark Theme",
                                            tint = Color.White
                                        )
                                    }
                                    if (!isDialogScreen) {
                                        IconButton(
                                            onClick = {
                                                if (currentDestination !in allowBottomBar) {
                                                    navController.popBackStack()
                                                } else if (navController.previousBackStackEntry != null) {
                                                    // Pop the backstack if there is a previous route
                                                    navController.popBackStack()
                                                    navigationSelectedItem = 0
                                                } else {
                                                    // Handle double back press to exit the app
                                                    if (doubleBackToExitPressedOnce) {
                                                        // Exit the app if back is pressed twice within 5 seconds
                                                        activity?.finish()
                                                    } else {
                                                        // Show the toast message and start a 5-second timer
                                                        doubleBackToExitPressedOnce = true
                                                        Toast.makeText(
                                                            context,
                                                            "برای خروج یک بار دیگر دکمه برگشت را بزنید",
                                                            Toast.LENGTH_SHORT
                                                        ).show()

                                                        // Reset the flag after 5 seconds using coroutine
                                                        coroutineScope.launch {
                                                            delay(5000)  // 5-second delay
                                                            doubleBackToExitPressedOnce = false
                                                        }
                                                    }
                                                }
                                            },
                                        ) {
                                            Icon(
                                                modifier = Modifier.rotate(180f),
                                                painter = painterResource(id = R.drawable.round_arrow_back_24),
                                                contentDescription = "Back",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = Color.White,
                            )
                        )
                    }
                },
                bottomBar = {
                    if (currentDestination in allowBottomBar) {
                        AnimatedVisibility(
                            visible = !isScrolled,
                            enter = fadeIn() + expandHorizontally() + slideInHorizontally(),
                            exit = fadeOut() + shrinkHorizontally() + slideOutHorizontally()
                        ) {
                            NavigationBar(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)),
                                containerColor = MaterialTheme.colorScheme.primary,
                            ) {
                                //getting the list of bottom navigation items for our data class
                                bottomNavigationItems().forEachIndexed { index, navigationItem ->
                                    NavigationBarItem(
                                        selected = index == navigationSelectedItem,
                                        label = {
                                            Text(
                                                text = context.getString(navigationItem.label),
                                                fontFamily = CustomFont
                                            )
                                        },
                                        icon = {
                                            BadgedBox(
                                                badge = {
                                                    if (navigationItem.badgeCount != 0) {
                                                        Badge(
                                                            containerColor = Color.White,
                                                            modifier = Modifier.border(
                                                                1.dp,
                                                                Color.Magenta,
                                                                CircleShape
                                                            )
                                                        ) {
                                                            Text(
                                                                text = navigationItem.badgeCount.toString(),
                                                                fontFamily = CustomFont,
                                                                color = Color.Black
                                                            )
                                                        }
                                                    }
                                                },
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = navigationItem.icon),
                                                    contentDescription = context.getString(
                                                        navigationItem.label
                                                    ),
                                                )
                                            }
                                        },
                                        onClick = {
                                            if (index != 1) navigationSelectedItem = index
                                            navController.navigate(navigationItem.route)
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color.Magenta,
                                            selectedTextColor = Color.Magenta,
                                            unselectedIconColor = Color.White,
                                            unselectedTextColor = Color.White,
                                            indicatorColor = Color.Transparent
                                        )
                                    )
                                }
                            }
                        }
                    }
                },
                floatingActionButton = {
                    AnimatedVisibility(visible = fabVisible && isDialogScreen) {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(Route.Users)
                            },
                            containerColor = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 0.dp
                            )
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add")
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.End,
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Route.Splash,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Route.Splash> {
                        SplashScreen(navController)
                    }
                    composable<Route.Login> {
                        LoginScreen(navController)
                    }
                    composable<Route.Register> {
                        RegisterScreen(navController)
                    }
                    composable<Route.Verify> { backStackEntry: NavBackStackEntry ->
                        val args = backStackEntry.toRoute<Route.Verify>()
                        VerifyScreen(navController, args.mobile)
                    }
                    composable<Route.Dialog> {
                        DialogScreen(navController)
                    }
                    composable<Route.Chat> { backStackEntry: NavBackStackEntry ->
                        val args = backStackEntry.toRoute<Route.Chat>()
                        args.id?.let {
                            ChatScreen(navController, it)
                        }
                    }
                    composable<Route.Profile> {
                        ProfileScreen(navController)
                    }
                    composable<Route.ProfileUserScreen> { backStackEntry: NavBackStackEntry ->
                        val args = backStackEntry.toRoute<Route.Chat>()
                        args.id?.let {
                            ProfileUserScreen(navController, it)
                        }
                    }
                    composable<Route.Setting> {
                        SettingScreen(navController)
                    }
                    composable<Route.Users> {
                        UsersScreen(navController)
                    }
                    composable<Route.ChatRoom> {
                        ChatRoomScreen(navController)
                    }
                }

                BackPressHandler(navController) { newItem ->
                    if (currentDestination in allowBottomBar)
                        navigationSelectedItem = newItem
                }
            }
        }
    }
}