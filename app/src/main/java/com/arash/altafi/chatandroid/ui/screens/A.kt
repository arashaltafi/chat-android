package com.arash.altafi.chatandroid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import kotlinx.coroutines.delay

@Composable
fun ManageDataList() {
    val context = LocalContext.current

    // Step 1: Create a mutableStateListOf to hold items of the data class
    val items = remember { mutableStateListOf<DataClass>() }

    // Step 2: Populate items initially using LaunchedEffect
    LaunchedEffect(Unit) {
        val initialItems = listOf(
            DataClass(id = 1, name = "Alice"),
            DataClass(id = 2, name = "Bob"),
            DataClass(id = 3, name = "Charlie")
        )
        items.clear()
        items.addAll(initialItems) // Add all initial items
    }

    // Step 3: Add new items at intervals
    LaunchedEffect(Unit) {
        delay(5000) // Simulate some delay or event
        items.add(0, DataClass(id = 4, name = "New Item 1")) // Prepend a new item
        Toast.makeText(context, "Added New Item 1", Toast.LENGTH_SHORT).show()

        delay(5000)
        items.add(0, DataClass(id = 5, name = "New Item 2")) // Prepend another item
        Toast.makeText(context, "Added New Item 2", Toast.LENGTH_SHORT).show()
    }

    // Step 4: Delete an item by ID
    LaunchedEffect(Unit) {
        delay(15000) // Simulate some delay or event
        val idToDelete = 2
        items.removeAll { it.id == idToDelete } // Delete by filtering
        Toast.makeText(context, "Deleted Item with ID $idToDelete", Toast.LENGTH_SHORT).show()
    }

    // Display the items in a LazyColumn
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        reverseLayout = true // Latest items will appear at the top
    ) {
        items(items.size) { index ->
            Text(
                textAlign = TextAlign.Center,
                fontFamily = CustomFont,
                fontSize = 18.sp,
                text = "ID: ${items[index].id}, Name: ${items[index].name}",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

// Step 5: Define your data class
data class DataClass(val id: Int, val name: String)
