package com.i2asolutions.athelo.presentation.model.health

import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.home.Feelings

data class Wellbeing(val id: Int, val feeling: Int, val occurrenceDate: Day) {
    fun toFeelings(): Feelings? {
        return when {
            feeling < 1 -> null
            feeling <= 25 -> Feelings.Sad
            feeling <= 75 -> Feelings.Ok
            feeling <= 100 -> Feelings.Good
            else -> null
        }
    }
}
