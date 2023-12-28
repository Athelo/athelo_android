package com.athelohealth.mobile.presentation.ui.caregiver.lostCaregiver

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class LostCaregiverViewState(override val isLoading: Boolean = false) : BaseViewState

sealed interface LostCaregiverEvent : BaseEvent {
    object OkButtonClick : LostCaregiverEvent
}

sealed interface LostCaregiverEffect : BaseEffect {
    object ShowRoleScreen : LostCaregiverEffect
}