package com.i2asolutions.athelo.presentation.model.settings

sealed interface SettingButton {
    val name: String
    val deeplink: String

    data class SimpleSettingButton(override val deeplink: String, override val name: String) :
        SettingButton

    data class CheckBoxSettingButton(
        override val name: String,
        override val deeplink: String,
        val isChecked: Boolean
    ) : SettingButton
}