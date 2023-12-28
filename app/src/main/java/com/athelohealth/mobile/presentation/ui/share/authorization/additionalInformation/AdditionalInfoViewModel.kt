package com.athelohealth.mobile.presentation.ui.share.authorization.additionalInformation

import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.model.enums.Enums
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.member.CreateProfileUseCase
import com.athelohealth.mobile.useCase.member.LoadStoredUserMailUseCase
import com.athelohealth.mobile.useCase.member.StoreUserEmailUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.useCase.websocket.ConnectWebSocketUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AdditionalInfoViewModel @Inject constructor(
    private val appManager: AppManager,
    private val enums: Enums,
    private val loadStoredUserMailUseCase: LoadStoredUserMailUseCase,
    private val createProfileUseCase: CreateProfileUseCase,
    private val storeUserUseCase: StoreUserUseCase,
    private val storedUserMailUseCase: StoreUserEmailUseCase,
    private val connectWebSocketUseCase: ConnectWebSocketUseCase,
) : BaseViewModel<AdditionalInfoEvent, AdditionalInfoEffect>() {
    private var selectedUserTypeId: String = EnumItem.EMPTY.id
    private var displayName: String = ""
    private var currentState =
        AdditionalInfoViewState(
            enableButton = validate(),
            userTypes = enums.userTypes.filter { it.id != "3" })
    private lateinit var userEmail: String

    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    init {
        loadData()
    }

    override fun loadData() {
        launchRequest {
            userEmail = loadStoredUserMailUseCase()
                ?: throw NullPointerException("Could not fetch user email address")
        }
    }

    override fun handleError(throwable: Throwable) {
        currentState = currentState.copy(isLoading = false)
        notifyStateChange()
        super.handleError(throwable)
    }

    override fun handleEvent(event: AdditionalInfoEvent) {
        when (event) {
            is AdditionalInfoEvent.InputValueChanged -> handleInput(
                event.inputType
            )
            AdditionalInfoEvent.SaveButtonClick -> createProfile()
        }
    }

    private fun createProfile() {
        if (validate()) {
            currentState = currentState.copy(isLoading = true)
            notifyStateChange()
            launchRequest {
                val user = createProfileUseCase(
                    email = userEmail,
                    displayName,
                    userTypeId = selectedUserTypeId
                )
                storeUserUseCase(user)
                storedUserMailUseCase(null)
                connectWebSocketUseCase()
                _effect.emit(
                    if (appManager.appType.value == AppType.Unknown) {
                        AdditionalInfoEffect.ShowActAsScreen(
                            true
                        )
                    } else AdditionalInfoEffect.ShowConnectSmartWatchScreen
                )
            }
        } else {
            currentState = currentState.copy(
                displayNameError = !validateDisplayName(),
                userTypeError = !validateUserType()
            )
            notifyStateChange()
        }
    }

    private fun handleInput(inputType: InputType) {
        when (inputType) {
            is InputType.DropDown -> {
                selectedUserTypeId = inputType.value
                currentState = currentState.copy(
                    enableButton = validate(),
                    selectedUserType = enums.userTypes.firstOrNull { it.id == selectedUserTypeId }
                        ?: EnumItem.EMPTY,
                )
            }
            is InputType.PersonName -> {
                displayName = inputType.value
                currentState = currentState.copy(
                    enableButton = validate(),
                    displayNameError = false
                )
            }
            else -> {}
        }
        notifyStateChange()
    }

    private fun notifyStateChange() {
        launchOnUI { _viewState.emit(currentState) }
    }

    private fun validateUserType(): Boolean {
        return selectedUserTypeId.isNotBlank()
    }

    private fun validate(): Boolean {
        return /*selectedUserTypeId.isNotBlank() && */validateDisplayName() && ::userEmail.isInitialized
    }

    private fun validateDisplayName() = displayName.isNotBlank()
}