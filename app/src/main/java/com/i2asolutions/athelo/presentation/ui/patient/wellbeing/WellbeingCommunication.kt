package com.i2asolutions.athelo.presentation.ui.patient.wellbeing

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.calendar.toDay
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState
import java.util.*

data class WellbeingViewState(
    override val isLoading: Boolean = false,
    //Calendar
    val initialDay: Day = Calendar.getInstance().toDay(),
    val selectedDay: Day = Calendar.getInstance().toDay(),
    //Feelings
    val feelingButtonEnabled: Boolean = false,
    val currentFeeling: Int = 1,
    val initialFeeling: Int? = null,
    //Symptoms
    val currentSymptoms: List<Symptom> = emptyList(),
    val allSymptoms: List<EnumItem> = emptyList(),
    val selectedSymptom: EnumItem = EnumItem.EMPTY,
    val removeConfirmPopup: Int? = null
) : BaseViewState

sealed interface WellbeingEvent : BaseEvent {
    class DayValueChanged(val day: Day) : WellbeingEvent
    class InputValueChanged(val inputType: InputType) : WellbeingEvent
    class FeelingValueChanged(val feeling: Int) : WellbeingEvent
    class SymptomRemovedClick(val symptomId: Int) : WellbeingEvent
    class SymptomRemovedConfirmClick(val symptomId: Int) : WellbeingEvent
    object SymptomRemovedCancelClick : WellbeingEvent
    class SymptomClick(val symptom: Symptom) : WellbeingEvent
    class SymptomAdded(val symptom: Symptom) : WellbeingEvent
    object SaveMyFeelingClick : WellbeingEvent
    object AddSymptomClick : WellbeingEvent
    object EditSymptomInformationClick : WellbeingEvent
    object SymptomChronologyClick : WellbeingEvent
    object BackButtonClick : WellbeingEvent
}

sealed interface WellbeingEffect : BaseEffect {
    class ShowSymptomChronologyScreen(val symptomId: String) : WellbeingEffect
    class ShowAddSymptomScreen(val symptom: Symptom, val day: Day) : WellbeingEffect
    class ShowInfoSymptomScreen(val symptom: Symptom) : WellbeingEffect
    object ShowPrevScreen : WellbeingEffect

}