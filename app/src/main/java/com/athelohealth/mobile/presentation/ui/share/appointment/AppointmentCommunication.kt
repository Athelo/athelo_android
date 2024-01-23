package com.athelohealth.mobile.presentation.ui.share.appointment


import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.profile.ProfileItems
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState


data class AppointmentViewState(
    override val isLoading: Boolean = false,
    val user: User = User(),
    val buttons: List<ProfileItems> = emptyList(),
    val showLogoutOutPopup: Boolean = false,
    val showDeletePopup: Boolean = false,
) : BaseViewState

sealed interface AppointmentEvent : BaseEvent {
    data class ButtonClick(val button: ProfileItems.Button) : AppointmentEvent
    object EditMyProfileClick : AppointmentEvent
    object DeleteButtonClick : AppointmentEvent
    object DeleteUserConfirmed : AppointmentEvent
    object LogoutUserConfirmed : AppointmentEvent
    object PopupCancelButtonClick : AppointmentEvent
    object GoBackClick : AppointmentEvent
}

sealed interface AppointmentEffect : BaseEffect {
    class ShowScreenFromDeeplink(val deeplink: String) : AppointmentEffect
    object ShowPrevScreen : AppointmentEffect
    object ShowEditProfileScreen : AppointmentEffect
    object ShowAuthorizationScreen: AppointmentEffect
}