package com.i2asolutions.athelo.utils

enum class DateTimeFormat(val format: String) {
    TheSameDay(format = "h:mm a"),
    TheSameWeek(format = "EEEE, h:mm a"),
    Other(format = "dd MMM yy, h:mm a")
}