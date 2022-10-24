package com.i2asolutions.athelo.presentation.model.home

import com.i2asolutions.athelo.presentation.model.health.Symptom

sealed interface HomeItems {
    @JvmInline
    value class HeaderHome(val message: String) : HomeItems
    data class ButtonHome(
        val icon: Int,
        val label: String,
        val arrow: Int? = null,
        val deeplink: String = ""
    ) : HomeItems

    data class WellbeingHome(val wellbeing: Feelings) : HomeItems
    data class SymptomsHome(val symptoms: List<Symptom>) : HomeItems

}