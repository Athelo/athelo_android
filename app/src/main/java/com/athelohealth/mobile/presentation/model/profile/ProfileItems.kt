package com.athelohealth.mobile.presentation.model.profile

sealed interface ProfileItems {
    @JvmInline
    value class Header(val text: Int) : ProfileItems
    data class Button(val icon: Int, val label: Int, val deeplink: String ="") : ProfileItems
    class DeleteButton(val icon: Int, val label: Int) : ProfileItems
}