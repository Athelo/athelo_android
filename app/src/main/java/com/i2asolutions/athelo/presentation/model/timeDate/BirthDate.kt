package com.i2asolutions.athelo.presentation.model.timeDate

import java.util.*

data class BirthDate(var year: Year, var month: Month) {
    val displayDate: String // Display full date MMMM, yyyy
        get() = "${month.longName}  ${year.yearValue}"
    val serverDate: String // Display full date yyyy-MM-dd
        get() = "${year.yearValue}-${month.value.toString().padStart(2, '0')}-15"

    companion object {
        val Default: BirthDate
            get() {
                val calendar = Calendar.getInstance().apply { add(Calendar.YEAR, -50) }
                return BirthDate(Year(calendar[Calendar.YEAR]), Month.January)
            }
    }
}