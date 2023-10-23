@file:Suppress("unused")

package com.i2asolutions.athelo.websocket

import android.os.Handler
import android.os.Looper
import com.i2asolutions.athelo.BuildConfig
import com.i2asolutions.athelo.extensions.debugPrint
import com.i2asolutions.athelo.presentation.model.chat.ConversationInfo
import com.i2asolutions.athelo.useCase.websocket.WebSocketSessionUseCases
import com.i2asolutions.athelo.websocket.constant.*
import com.i2asolutions.athelo.websocket.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.reflect.KClass


class WebSocketManager(private val webSocketUseCases: WebSocketSessionUseCases) : CoroutineScope {

    private val _eventFlow: MutableSharedFlow<ConversationInfo> =
        MutableSharedFlow(extraBufferCapacity = 1)
    val eventFlow: Flow<ConversationInfo> = _eventFlow

    private var sessionToken: String = ""
    private var webSocket: WebSocket? = null
    private val jsonConverter = Json {
        encodeDefaults = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    private var listenerJob: Job? = null
    private val activeCallbacks: ArrayList<WebSocketSuspendCallback<*>> = arrayListOf()

    private val reconnectHandler: Handler = Handler(Looper.getMainLooper())

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            if (throwable is SocketTimeoutException || throwable is UnknownHostException) {
                webSocket?.let { webSocket ->
                    webSocketListener.onFailure(webSocket, throwable, null)
                }
            }
            throwable.printStackTrace()
        }

    suspend fun connect() {
        runCatching { webSocketUseCases.openChatSession() }
            .onSuccess {
                sessionToken = it
                openChatSession()
                startListeningToMessages()
            }
            .onFailure {
                scheduleReconnect()
            }
    }

    suspend fun disconnect() {
        webSocketUseCases.closeChatSession(sessionToken)
        stopListeningToMessages()
        webSocket?.close(WEB_SOCKET_CLOSE_CODE_NORMAL, null)
    }

    fun sendMessage(chatId: String, message: String) {
        val request = jsonConverter.encodeToString(
            RoutableRequestDto(
                chatRoomIdentifier = chatId,
                message = message
            )
        )
        webSocket?.send(request)
    }

    suspend fun getChatHistory(
        chatId: String,
        from: Long,
        limit: Int = 100
    ): ConversationInfo.ConversationMessageList {
        return createSuspendCallback(
            GET_HISTORY,
            listOf(chatId),
            ConversationInfo.ConversationMessageList::class
        ) {
            webSocket?.send(
                jsonConverter.encodeToString(
                    GetHistoryRequestDto(
                        chatRoomIdentifier = chatId,
                        fromMessageTimestampIdentifier = from,
                        limit = limit
                    )
                )
            )
        }
    }

    fun setLastMessageRead(chatId: String, messageTimestampId: String) {
        val request = jsonConverter.encodeToString(
            SetLastMessageReadRequestDto(
                chatRoomIdentifier = chatId,
                messageTimestampIdentifier = messageTimestampId.toLong(),
            )
        )
        webSocket?.send(request)
    }

    fun getLastMessageRead(chatId: String) {
        val request = jsonConverter.encodeToString(
            GetLastMessageReadRequestDto(
                chatRoomIdentifier = chatId
            )
        )
        webSocket?.send(request)
    }

    suspend fun getLastChatRoomMessage(chatIds: List<String>): ConversationInfo.ConversationLastMessage {
        return createSuspendCallback(
            GET_LAST_CHAT_ROOM_MESSAGE,
            chatIds,
            ConversationInfo.ConversationLastMessage::class
        ) {
            webSocket?.send(
                jsonConverter.encodeToString(
                    GetLastChatRoomMessageRequestDto(
                        chatRoomIdentifiers = chatIds.toTypedArray()
                    )
                )
            )
        }
    }

    suspend fun getUnreadMessagesCount(chatIds: List<String>): ConversationInfo.ConversationUnreadMessageCount {
        return createSuspendCallback(
            GET_UNREAD_MESSAGES_COUNT,
            chatIds,
            ConversationInfo.ConversationUnreadMessageCount::class
        ) {
            webSocket?.send(
                jsonConverter.encodeToString(
                    GetUnreadMessagesCountRequestDto(
                        chatRoomIdentifiers = chatIds.toTypedArray()
                    )
                )
            )
        }
    }

    fun observeNewMessages(chatId: String): Flow<ConversationInfo.ConversationMessage> {
        return eventFlow
            .mapNotNull { it as? ConversationInfo.ConversationMessage }
            .filter { it.chatId == chatId }
            .filterNot { it.type == SET_LAST_MESSAGE_READ }
    }

    private fun openChatSession() {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .callTimeout(5, TimeUnit.SECONDS)
            .pingInterval(10, TimeUnit.SECONDS)
            .addTimeoutInterceptor()
            .addTokenInterceptor(sessionToken)
            .build()

        runCatching {
            val request = Request.Builder().url(BuildConfig.BASE_WSS).build()
            webSocket = okHttpClient.newWebSocket(request, webSocketListener)
        }.onFailure { it.printStackTrace() }

    }

    private fun OkHttpClient.Builder.addTokenInterceptor(token: String): OkHttpClient.Builder {
        addInterceptor { chain ->
            try {
                val request = chain.request().newBuilder()
                request.addHeader("X-TOKEN", token)
                chain.proceed(request.build())
            } catch (e: Exception) {
                chain.proceed(chain.request())
            }
        }
        return this
    }

    private fun OkHttpClient.Builder.addTimeoutInterceptor(): OkHttpClient.Builder {
        addInterceptor { chain ->
            try {
                val response: Response = chain.proceed(chain.request())
                val bodyString = response.body!!.string()

                response.newBuilder()
                    .body(bodyString.toResponseBody(response.body?.contentType()))
                    .build()
            } catch (exception: SocketTimeoutException) {
                exception.printStackTrace()
                webSocket?.let { webSocket ->
                    webSocketListener.onFailure(webSocket, Exception(), null)
                }
                chain.proceed(chain.request())
            }
        }
        return this
    }

    private fun scheduleReconnect() {
        reconnectHandler.removeCallbacksAndMessages(null)
        reconnectHandler.postDelayed({
            launch { connect() }
        }, 2000L)
    }

    private fun startListeningToMessages() {
        stopListeningToMessages()
        listenerJob =
            launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }) {
                _eventFlow.collect {
                    if (!isActive) return@collect
                    handleMessage(it)
                }
            }
    }

    private fun stopListeningToMessages() {
        listenerJob?.cancel()
        listenerJob = null
    }

    private fun handleMessage(conversation: ConversationInfo) {
        val clazz = conversation::class
        var filtered = activeCallbacks.asSequence()
            .filter { it.clazz == clazz }
            .filter { it.operation == conversation.type }
        val receivedChatIds = hashSetOf<String>()
        when (conversation) {
            is ConversationInfo.ConversationMessage -> {
                receivedChatIds.add(conversation.chatId)
            }
            is ConversationInfo.ConversationMessageList -> {
                receivedChatIds.addAll(conversation.payload.map { it.chatId })
            }
            is ConversationInfo.ConversationUnreadMessageCount -> {
                receivedChatIds.addAll(conversation.payload.map { it.chatId })
            }
            is ConversationInfo.ConversationLastMessage -> {
                receivedChatIds.addAll(conversation.payload.map { it.chatId })
            }
            is ConversationInfo.ConversationError -> {

            }
        }
        filtered = filtered.filter { it.chatIds == null || it.chatIds.containsAll(receivedChatIds) }
        filtered.forEach { it.onResult(conversation) }
        activeCallbacks.removeAll(filtered.toSet())
    }

    private suspend inline fun <reified T : ConversationInfo> createSuspendCallback(
        operation: String,
        chatId: List<String>?,
        clazz: KClass<T>,
        crossinline startBlock: () -> Unit
    ) = suspendCancellableCoroutine { coroutine ->
        val callback = object : WebSocketSuspendCallback<T>(operation, chatId, clazz) {
            override fun onResult(result: ConversationInfo) {
                activeCallbacks.remove(this)
                if (coroutine.isActive && result is T)
                    coroutine.resume(result)
                else if (coroutine.isActive)
                    coroutine.resumeWith(Result.failure(ClassNotFoundException()))
            }
        }
        activeCallbacks.add(callback)
        startBlock()
    }

    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            debugPrint("onOpen: ${response.message}")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            debugPrint("onClosed: $reason with code: $code")
            CoroutineScope(coroutineContext).launch {
                if (code != WEB_SOCKET_CLOSE_CODE_NORMAL)
                    webSocketUseCases.closeChatSession(sessionToken)
                if (code > WEB_SOCKET_CLOSE_CODE_NORMAL) scheduleReconnect()
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            debugPrint("onMessage: $text")
            val obj = WebSocketParser.parseMessage(text, jsonConverter) ?: return
            launch {
                _eventFlow.emit(obj)
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            debugPrint("onClosing: $reason with code: $code")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            onClosed(webSocket, WEB_SOCKET_CLOSE_CODE_PROTOCOL_ERROR, t.message ?: "")
            debugPrint("onFailure: ${response?.message}")
        }
    }

    private abstract class WebSocketSuspendCallback<T : ConversationInfo>(
        val operation: String,
        val chatIds: List<String>?,
        val clazz: KClass<T>
    ) {
        abstract fun onResult(result: ConversationInfo)
        override fun toString(): String {
            return "WebSocketSuspendCallback(operation='$operation', chatIds=$chatIds, clazz=$clazz)"
        }


    }
}
