package com.i2asolutions.athelo.presentation.ui.settings

import com.i2asolutions.athelo.presentation.model.settings.SettingButton
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.utils.DEEPLINK_EDIT_PROFILE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : BaseViewModel<SettingsEvent, SettingsEffect>() {
    private var currentState = SettingsViewState(isLoading = false, buttons = prepareList())


    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {

    }

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

    private fun notifyStateChanged(newState: SettingsViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}