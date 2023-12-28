@file:Suppress("unused")

package com.athelohealth.mobile.websocket.constant

/**
 * 1000 indicates a normal closure, meaning that the purpose for
 * which the connection was established has been fulfilled.
 */
const val WEB_SOCKET_CLOSE_CODE_NORMAL = 1000

/**
 * 1001 indicates that an endpoint is "going away", such as a server
 * going down or a browser having navigated away from a page.
 */
const val WEB_SOCKET_CLOSE_CODE_GOING_AWAY = 1001

/**
 * 1002 indicates that an endpoint is terminating the connection due
 * to a protocol error.
 */
const val WEB_SOCKET_CLOSE_CODE_PROTOCOL_ERROR = 1002

/**
 * 1003 indicates that an endpoint is terminating the connection
 * because it has received a type of data it cannot accept (e.g., an
 * endpoint that understands only text data MAY send this if it
 * receives a binary message).
 */
const val WEB_SOCKET_CLOSE_CODE_DATA_ERROR = 1003