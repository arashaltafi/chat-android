package com.arash.altafi.chatandroid.data.repository

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import javax.inject.Inject

class SocketRepository @Inject constructor(
    private val serverUrl: String
) {

    private var socket: Socket? = null
    var isConnected: ((Boolean) -> Unit)? = null
    var onError: ((Boolean) -> Unit)? = null
    private val eventListeners = mutableMapOf<String, (Any) -> Unit>()

    // Create a flag to track initial connection error
    var initialConnectionErrorOccurred = false

    fun connect() {
        val options = IO.Options.builder()
            .setTransports(TRANSPORTS)
            .setUpgrade(true)
            .setRememberUpgrade(false)
            .build()

        options.forceNew = true
        options.reconnection = true
        options.reconnectionAttempts = 1000
        options.reconnectionDelay = 1000
        options.timeout = 10000

        socket = IO.socket(serverUrl, options)

        socket?.on(Socket.EVENT_CONNECT, onConnect)
        socket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
        socket?.on(Socket.EVENT_CONNECT_ERROR, onConnectError)

        socket?.connect()
    }

    fun send(key: String, data: Any? = null) {
        socket?.emit(key, data)
    }

    fun onReceivedData(key: String, listener: (Any) -> Unit) {
        eventListeners[key] = listener
        socket?.on(key) { args ->
            val eventData = args[0] // Assuming the payload is the first element
            listener.invoke(eventData)
        }
    }

    fun emitAndReceive(key: String, data: Any? = null, listener: (Any) -> Unit) {
        socket?.emit(key, data)

        eventListeners[key] = listener
        socket?.once(key) { args ->
            val eventData = args[0] // Assuming the payload is the first element
            listener.invoke(eventData)
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket?.off(Socket.EVENT_CONNECT, onConnect)
        socket?.off(Socket.EVENT_DISCONNECT, onDisconnect)
        eventListeners.keys.forEach { key ->
            socket?.off(key)
        }
        eventListeners.clear()
        socket = null
    }

    private val onConnect = Emitter.Listener {
        Log.i("test123321", "onConnect()")
        // Handle connection
        isConnected?.invoke(true)
        onError?.invoke(false)

        // Reset the initialConnectionErrorOccurred flag when connected
        initialConnectionErrorOccurred = false
    }

    private val onDisconnect = Emitter.Listener {
        Log.i("test123321", "onDisconnect()")
        // Handle disconnection
        isConnected?.invoke(false)
    }

    private val onConnectError = Emitter.Listener {
        Log.i("test123321", "onConnectError()")
        // Handle Connect Error
        if (!initialConnectionErrorOccurred) {
            onError?.invoke(true)
            initialConnectionErrorOccurred = true
        }
    }

    companion object {
//        private val TRANSPORTS = arrayOf(WebSocket.NAME)
        private val TRANSPORTS = arrayOf(WebSocket.NAME, "polling")
    }
}