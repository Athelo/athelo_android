package com.athelohealth.mobile.network.dto.health


import com.athelohealth.mobile.extensions.toDate
import com.athelohealth.mobile.presentation.model.calendar.Day
import com.athelohealth.mobile.presentation.model.calendar.toDay
import com.athelohealth.mobile.presentation.model.health.Wellbeing
import com.athelohealth.mobile.presentation.model.timeDate.Month
import com.athelohealth.mobile.utils.consts.DATE_FORMAT_SIMPLE_DAY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WellbeingDto(
    @SerialName("general_feeling")
    val generalFeeling: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("note")
    val note: String? = null,
    @SerialName("occurrence_date")
    val occurrenceDate: String? = null
) {
    fun toWellbeing(): Wellbeing {
        return Wellbeing(
            id ?: -1, generalFeeling ?: 1, occurrenceDate = occurrenceDate.toDate(
                DATE_FORMAT_SIMPLE_DAY
            )?.toDay() ?: Day(1, Month.January, 1990, "", "", false)
        )
    }
}