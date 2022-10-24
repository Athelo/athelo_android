package com.i2asolutions.athelo.presentation.model.application

data class FAQ(val header: String, val content: String) {
    val isEmpty: Boolean
        get() = !header.isNullOrBlank() && !content.isNullOrBlank()
}
