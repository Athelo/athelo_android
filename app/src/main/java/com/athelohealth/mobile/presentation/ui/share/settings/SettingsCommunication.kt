package com.athelohealth.mobile.presentation.ui.share.settings

import com.athelohealth.mobile.presentation.model.settings.SettingButton
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

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