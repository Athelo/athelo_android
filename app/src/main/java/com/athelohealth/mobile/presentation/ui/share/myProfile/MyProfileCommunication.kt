package com.athelohealth.mobile.presentation.ui.share.myProfile


import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.profile.ProfileItems
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState


data class MyProfileViewState(
    override val isLoading: Boolean = false,
    val user: User = User(),
    val buttons: List<ProfileItems> = emptyList(),
    val showLogoutOutPopup: Boolean = false,
    val showDeletePopup: Boolean = false,
) : BaseViewState

sealed interface MyProfileEvent : BaseEvent {
    data class ButtonClick(val button: ProfileItems.Button) : MyProfileEvent
    object EditMyProfileClick : MyProfileEvent
    object DeleteButtonClick : MyProfileEvent
    object DeleteUserConfirmed : MyProfileEvent
    object LogoutUserConfirmed : MyProfileEvent
    object PopupCancelButtonClick : MyProfileEvent
    object GoBackClick : MyProfileEvent
}

sealed interface MyProfileEffect : BaseEffect {
    class ShowScreenFromDeeplink(val deeplink: String) : MyProfileEffect
    object ShowPrevScreen : MyProfileEffect
    object ShowEditProfileScreen : MyProfileEffect
    object ShowAuthorizationScreen: MyProfileEffect
}