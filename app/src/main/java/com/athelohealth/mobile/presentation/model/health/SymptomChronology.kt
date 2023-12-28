package com.athelohealth.mobile.presentation.model.health

import com.athelohealth.mobile.extensions.displayAsString
import com.athelohealth.mobile.presentation.model.calendar.Day
import com.athelohealth.mobile.presentation.model.calendar.toDay
import com.athelohealth.mobile.presentation.model.home.Feelings
import java.util.*

data class SymptomChronology(val symptoms: List<Symptom>, val feeling: Feelings?, val date: Date):SymptomChronologyItem {
    val day: Day = date.toDay()
    val yearHeader: String = date.displayAsString("yyyy 'year'")
    override val id: Any
        get() = day

    val symptomNames: String =
        symptoms.asSequence().sortedBy { it.name.lowercase() }.distinctBy { it.name }
            .joinToString(", ") { it.name }
}

sealed interface SymptomChronologyItem {
    val id: Any
}

data class SymptomChronologyHeader(val header: String, override val id: Any = header) :
    SymptomChronologyItem
