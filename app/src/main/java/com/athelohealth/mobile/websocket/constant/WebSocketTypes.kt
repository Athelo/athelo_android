package com.athelohealth.mobile.websocket.constant

import androidx.annotation.StringDef

    const val ERROR = "ERROR"
    const val GET_LAST_CHAT_ROOM_MESSAGE = "GET_LAST_CHAT_ROOM_MESSAGE"
    const val GET_UNREAD_MESSAGES_COUNT = "GET_UNREAD_MESSAGES_COUNT"
    const val GET_LAST_MESSAGES_READ = "GET_LAST_MESSAGES_READ"
    const val SET_LAST_MESSAGE_READ = "SET_LAST_MESSAGE_READ"
    const val GET_HISTORY = "GET_HISTORY"
    const val ROUTABLE = "ROUTABLE" // used to send messages
    const val SYSTEM_ROUTABLE = "SYSTEM_ROUTABLE" // used to send messages

    @StringDef(GET_LAST_CHAT_ROOM_MESSAGE, ERROR, GET_UNREAD_MESSAGES_COUNT,
        GET_LAST_MESSAGES_READ, SET_LAST_MESSAGE_READ, GET_HISTORY, ROUTABLE, SYSTEM_ROUTABLE)
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class WebSocketType