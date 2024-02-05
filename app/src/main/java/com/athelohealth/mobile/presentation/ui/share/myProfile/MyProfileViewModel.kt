package com.athelohealth.mobile.presentation.ui.share.myProfile

import com.athelohealth.mobile.R
import com.athelohealth.mobile.network.dto.member.CancerStatus
import com.athelohealth.mobile.presentation.model.member.IdentityType
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.profile.ProfileItems
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.member.DeleteUserUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.useCase.member.signOut.LogOutUseCase
import com.athelohealth.mobile.utils.*
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadMyProfileUseCase: LoadMyProfileUseCase,
    private val storeUser: StoreUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val deleteUser: DeleteUserUseCase,
) : BaseViewModel<MyProfileEvent, MyProfileEffect, MyProfileViewState>(MyProfileViewState(
    isLoading = true,
    user = User(),
    emptyList()
)) {
    private var showChangePasswordButton = true
    private var showMyDeviceButton = false

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        launchRequest {
            val treatmentStatus = loadMyProfileUseCase.treatmentStatus().cancerStatus
            val user: User = loadMyProfileUseCase()?.also { storeUser(it) }?.copy(cancerStatus = treatmentStatus)
                ?: throw AuthorizationException("Session expired")
            showChangePasswordButton = appManager.authenticationType == IdentityType.Native
            showMyDeviceButton = user.fitBitConnected == true

            notifyStateChange(
                currentState.copy(
                    user = user,
                    isLoading = false,
                    buttons = generateButtons()
                )
            )
        }
    }

    override fun handleEvent(event: MyProfileEvent) {
        when (event) {
            is MyProfileEvent.ButtonClick -> handleButtonClick(event.button)
            MyProfileEvent.DeleteButtonClick -> {
                notifyStateChange(currentState.copy(showDeletePopup = true))
            }
            MyProfileEvent.EditMyProfileClick -> notifyEffectChanged(MyProfileEffect.ShowEditProfileScreen)
            MyProfileEvent.GoBackClick -> notifyEffectChanged(MyProfileEffect.ShowPrevScreen)
            MyProfileEvent.LogoutUserConfirmed -> launchRequest {
                logOutUseCase()
                notifyEffectChanged(MyProfileEffect.ShowAuthorizationScreen)
            }
            MyProfileEvent.PopupCancelButtonClick -> {
                notifyStateChange(
                    currentState.copy(
                        showDeletePopup = false,
                        showLogoutOutPopup = false
                    )
                )
            }
            MyProfileEvent.DeleteUserConfirmed -> launchRequest {
                notifyStateChange(currentState.copy(isLoading = true))
                if (deleteUser()) {
                    logOutUseCase()
                    notifyEffectChanged(MyProfileEffect.ShowAuthorizationScreen)
                } else {
                    notifyStateChange(currentState.copy(isLoading = true))
                }
            }
        }
    }

    private fun handleButtonClick(button: ProfileItems.Button) {
        when (button.deeplink) {
            DEEPLINK_LOG_OUT -> {
                notifyStateChange(currentState.copy(showLogoutOutPopup = true))
            }
            else -> notifyEffectChanged(MyProfileEffect.ShowScreenFromDeeplink(button.deeplink))
        }

    }

    private fun generateButtons(): List<ProfileItems> {
        return if (appManager.appType.value is AppType.Caregiver) buildCaregiverItems() else buildPatientItems()
    }

    private fun buildPatientItems(): List<ProfileItems> =
        buildList {
//            add(ProfileItems.Header(R.string.My_roles))
//            add(
//                ProfileItems.Button(
//                    R.drawable.caregiver,
//                    R.string.Act_as,
//                    DEEPLINK_SELECT_ROLE.format(false)
//                )
//            )
            add(ProfileItems.Header(R.string.My_Health))
            add(
                ProfileItems.Button(
                    R.drawable.ic_smile,
                    R.string.Track_My_Wellbeing,
                    DEEPLINK_WELLBEING
                )
            )
            add(
                ProfileItems.Button(
                    R.drawable.ic_symptoms,
                    R.string.My_Symptoms,
                    DEEPLINK_MY_SYMPTOMS
                )
            )
            add(ProfileItems.Header(R.string.Other))
            if (showMyDeviceButton)
                add(
                    ProfileItems.Button(
                        R.drawable.watch_status, R.string.My_Device,
                        DEEPLINK_MY_DEVICE
                    )
                )
            if (showChangePasswordButton)
                add(
                    ProfileItems.Button(
                        R.drawable.ic_change_password, R.string.Change_Password,
                        DEEPLINK_CHANGE_PASSWORD
                    )
                )
            add(ProfileItems.Button(R.drawable.ic_log_out, R.string.Log_Out, DEEPLINK_LOG_OUT))
            add(ProfileItems.DeleteButton(R.drawable.ic_delete_account, R.string.Delete_An_Account))
        }

    private fun buildCaregiverItems(): List<ProfileItems> = buildList {
        add(ProfileItems.Header(R.string.My_roles))
        add(
            ProfileItems.Button(
                R.drawable.caregiver,
                R.string.Act_as,
                DEEPLINK_SELECT_ROLE.format(false)
            )
        )
        add(ProfileItems.Header(R.string.Other))
        if (showMyDeviceButton)
            add(
                ProfileItems.Button(
                    R.drawable.watch_status, R.string.My_Device,
                    DEEPLINK_MY_DEVICE
                )
            )
        if (showChangePasswordButton)
            add(
                ProfileItems.Button(
                    R.drawable.ic_change_password, R.string.Change_Password,
                    DEEPLINK_CHANGE_PASSWORD
                )
            )
        add(ProfileItems.Button(R.drawable.ic_log_out, R.string.Log_Out, DEEPLINK_LOG_OUT))
        add(ProfileItems.DeleteButton(R.drawable.ic_delete_account, R.string.Delete_An_Account))
    }
}