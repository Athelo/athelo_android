package com.i2asolutions.athelo.presentation.model.health

import com.i2asolutions.athelo.extensions.displayAsString
import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.calendar.toDay
import com.i2asolutions.athelo.presentation.model.home.Feelings
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
