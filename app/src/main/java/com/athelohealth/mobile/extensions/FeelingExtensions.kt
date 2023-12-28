package com.athelohealth.mobile.extensions

fun Int.normalizeValue(): Int {
    return this/*100 - when (this) {
        in 0..25 -> 0 // Sad
        in 25..75 -> 50 // Ok
        else -> 99 // Good
    }*/
}