package com.i2asolutions.athelo.presentation.model.base

sealed interface InputType {
    val value: String

    data class Password(override val value: String) : InputType
    data class PersonName(override val value: String) : InputType
    data class NewPassword(override val value: String) : InputType
    data class ConfirmPassword(override val value: String) : InputType
    data class Email(override val value: String) : InputType
    data class DropDown(override val value: String) : InputType
    data class Calendar(override val value: String) : InputType
    data class PhoneNumber(override val value: String) : InputType
    data class Text(override val value: String) : InputType
}