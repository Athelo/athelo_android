package com.i2asolutions.athelo.presentation.ui.caregiver.lostCaregiver

import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class LostCaregiverViewState(override val isLoading: Boolean = false) : BaseViewState

sealed interface LostCaregiverEvent : BaseEvent {
    object OkButtonClick : LostCaregiverEvent
}

sealed interface LostCaregiverEffect : BaseEffect {
    object ShowRoleScreen : LostCaregiverEffect
}