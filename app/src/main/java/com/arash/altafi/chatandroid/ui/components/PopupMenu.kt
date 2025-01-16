package com.arash.altafi.chatandroid.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.arash.altafi.chatandroid.ui.theme.CustomFont

data class PopupMenuItem(
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun PopupMenu(
    showMenu: Boolean,
    onHideMenu: () -> Unit,
    menuItems: List<PopupMenuItem>
) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = {
            onHideMenu.invoke()
        },
    ) {
        menuItems.forEach { menuItem ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = menuItem.label,
                        color = Color.White,
                        fontFamily = CustomFont,
                        fontSize = 14.sp
                    )
                },
                onClick = {
                    menuItem.onClick()
                    onHideMenu()
                }
            )
        }
    }
}