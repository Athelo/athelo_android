package com.i2asolutions.athelo.presentation.model.timeDate

enum class Month(val value: Int, val shortName: String, val longName: String) {
    January(1, "Jan", "January"),
    February(2, "Feb", "February"),
    March(3, "Mar", "March"),
    April(4, "Apr", "April"),
    May(5, "May", "May"),
    June(6, "Jun", "June"),
    July(7, "Jul", "July"),
    August(8, "Aug", "August"),
    September(9, "Sep", "September"),
    October(10, "Oct", "October"),
    November(11, "Nov", "November"),
    December(12, "Dec", "December"),
}

fun parse(int: Int): Month {
    return Month.values().firstOrNull { it.value == int } ?: Month.January
}