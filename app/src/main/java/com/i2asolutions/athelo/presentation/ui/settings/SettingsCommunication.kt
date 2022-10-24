package com.i2asolutions.athelo.presentation.ui.settings

import com.i2asolutions.athelo.presentation.model.settings.SettingButton
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class SettingsViewState(
    override val isLoading: Boolean = false,
    val buttons: List<SettingButton>
) : BaseViewState

sealed interface SettingsEvent : BaseEvent {
    data class ButtonClick(val deeplink: String) : SettingsEvent
    object BackButtonClick : SettingsEvent
}

sealed interface SettingsEffect : BaseEffect {
    object ShowPrevScreen : SettingsEffect
    object ShowPersonalInformationScreen : SettingsEffect
    object ShowAboutUsScreen : SettingsEffect
    object ShowTermsOfUseScreen : SettingsEffect
    object ShowPrivacyPolicyScreen : SettingsEffect
}