package com.i2asolutions.athelo.presentation.ui.share.selectRole

import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class SelectRoleViewState(
    override val isLoading: Boolean = false,
    val showBackButton: Boolean = false,
) : BaseViewState

sealed interface SelectRoleEvent : BaseEvent {
    object ActAsPatientClick : SelectRoleEvent
    object ActAsCaregiverClick : SelectRoleEvent
    object BackButtonClick : SelectRoleEvent
}

sealed interface SelectRoleEffect : BaseEffect {
    object ShowPrevScreen : SelectRoleEffect

    data class ShowCaregiverListScreen(val initialFlow: Boolean) : SelectRoleEffect
    data class ShowPatientListScreen(val initialFlow: Boolean) : SelectRoleEffect
}