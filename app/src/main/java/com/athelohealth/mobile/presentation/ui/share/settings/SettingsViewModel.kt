package com.athelohealth.mobile.presentation.ui.share.settings

import com.athelohealth.mobile.presentation.model.settings.SettingButton
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.utils.DEEPLINK_EDIT_PROFILE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : BaseViewModel<SettingsEvent, SettingsEffect, SettingsViewState>(SettingsViewState(isLoading = false, buttons = emptyList())) {
    init {
        notifyStateChange(SettingsViewState(isLoading = false, buttons = prepareList()))
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {}

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ButtonClick -> handleButtonEvent(event.deeplink)
            SettingsEvent.BackButtonClick -> notifyEffectChanged(SettingsEffect.ShowPrevScreen)
        }
    }

    private fun handleButtonEvent(deeplink: String) {
        when (deeplink) {
            DEEPLINK_EDIT_PROFILE -> notifyEffectChanged(SettingsEffect.ShowPersonalInformationScreen)
            "About us" -> notifyEffectChanged(SettingsEffect.ShowAboutUsScreen)
            "Privacy Policy" -> notifyEffectChanged(SettingsEffect.ShowPrivacyPolicyScreen)
            "Terms of Use" -> notifyEffectChanged(SettingsEffect.ShowTermsOfUseScreen)
        }
    }

    private fun prepareList(): List<SettingButton> = buildList {
        add(
            SettingButton.SimpleSettingButton(
                name = "Personal Information",
                deeplink = DEEPLINK_EDIT_PROFILE
            )
        )
        add(SettingButton.SimpleSettingButton(name = "About us", deeplink = "About us"))
        add(SettingButton.SimpleSettingButton(name = "Privacy Policy", deeplink = "Privacy Policy"))
    }
}