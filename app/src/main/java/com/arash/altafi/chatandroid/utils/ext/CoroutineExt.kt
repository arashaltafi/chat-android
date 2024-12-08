package com.arash.altafi.chatandroid.utils.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

suspend fun <T> withCompute(
    block: suspend CoroutineScope.() -> T,
) = withContext(Dispatchers.Default) { block(this) }

suspend fun <T> withIO(
    block: suspend CoroutineScope.() -> T,
) = withContext(Dispatchers.IO) { block(this) }

fun <T> CoroutineScope.launchCompute(
    block: suspend CoroutineScope.() -> T
) = launch(Dispatchers.Default) { block() }

fun <T> CoroutineScope.launchIO(
    block: suspend CoroutineScope.() -> T
) = launch(Dispatchers.IO) { block() }

fun <T> CoroutineScope.asyncCompute(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
) = async(Dispatchers.Default, start, block)

fun <T> CoroutineScope.asyncIO(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
) = async(Dispatchers.IO, start, block)

fun ViewModel.viewModelCompute(
    block: suspend CoroutineScope.() -> Unit,
) = viewModelScope.launchCompute(block)

fun ViewModel.viewModelIO(
    block: suspend CoroutineScope.() -> Unit,
) = viewModelScope.launchIO(block)

fun <T> runBlockingCompute(block: suspend CoroutineScope.() -> T) {
    runBlocking(Dispatchers.Default) { block() }
}

fun <T> runBlockingIO(block: suspend CoroutineScope.() -> T) {
    runBlocking(Dispatchers.IO) { block() }
}