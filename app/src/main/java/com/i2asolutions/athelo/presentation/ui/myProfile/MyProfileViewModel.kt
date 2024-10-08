package com.i2asolutions.athelo.presentation.ui.myProfile

import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.member.IdentityType
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.model.profile.ProfileItems
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.member.CheckAuthorizationIdentityUseCase
import com.i2asolutions.athelo.useCase.member.DeleteUserUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.useCase.member.StoreUserUseCase
import com.i2asolutions.athelo.useCase.member.signOut.LogOutUseCase
import com.i2asolutions.athelo.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val loadMyProfileUseCase: LoadMyProfileUseCase,
    private val storeUser: StoreUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val deleteUser: DeleteUserUseCase,
    private val checkAuthorizationIdentity: CheckAuthorizationIdentityUseCase,
) : BaseViewModel<MyProfileEvent, MyProfileEffect>() {
    private var currentState = MyProfileViewState(
        isLoading = true,
        user = User(),
        emptyList()
    )
    private var showChangePasswordButton = true
    private var showMyDeviceButton = false

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {
        launchRequest {
            val user: User = loadMyProfileUseCase()?.also { storeUser(it) }
                ?: throw AuthorizationException("Session expired")
            showChangePasswordButton =
                checkAuthorizationIdentity().any { it.type == IdentityType.Native }
            showMyDeviceButton = user.fitBitConnected == true
            notifyStateChanged(
                currentState.copy(
                    user = user,
                    isLoading = false,
                    buttons = generateButtons()
                )
            )
        }
    }

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun handleEvent(event: MyProfileEvent) {
        when (event) {
            is MyProfileEvent.ButtonClick -> handleButtonClick(event.button)
            MyProfileEvent.DeleteButtonClick -> {
                notifyStateChanged(currentState.copy(showDeletePopup = true))
            }
            MyProfileEvent.EditMyProfileClick -> notifyEffectChanged(MyProfileEffect.ShowEditProfileScreen)
            MyProfileEvent.GoBackClick -> notifyEffectChanged(MyProfileEffect.ShowPrevScreen)
            MyProfileEvent.LogoutUserConfirmed -> launchRequest {
                logOutUseCase()
                notifyEffectChanged(MyProfileEffect.ShowAuthorizationScreen)
            }
            MyProfileEvent.PopupCancelButtonClick -> {
                notifyStateChanged(
                    currentState.copy(
                        showDeletePopup = false,
                        showLogoutOutPopup = false
                    )
                )
            }
            MyProfileEvent.DeleteUserConfirmed -> launchRequest {
                notifyStateChanged(currentState.copy(isLoading = true))
                if (deleteUser()) {
                    logOutUseCase()
                    notifyEffectChanged(MyProfileEffect.ShowAuthorizationScreen)
                } else {
                    notifyStateChanged(currentState.copy(isLoading = true))
                }
            }
        }
    }

    private fun handleButtonClick(button: ProfileItems.Button) {
        when (button.deeplink) {
            DEEPLINK_LOG_OUT -> {
                notifyStateChanged(currentState.copy(showLogoutOutPopup = true))
            }
            else -> notifyEffectChanged(MyProfileEffect.ShowScreenFromDeeplink(button.deeplink))
        }

    }

    private fun notifyStateChanged(state: MyProfileViewState = currentState) {
        currentState = state
        launchOnUI { _state.emit(currentState) }
    }

    private fun generateButtons(): List<ProfileItems> {

        return buildList {
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
    }
}