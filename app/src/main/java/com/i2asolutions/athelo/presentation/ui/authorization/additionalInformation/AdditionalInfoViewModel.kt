package com.i2asolutions.athelo.presentation.ui.authorization.additionalInformation

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.model.enums.Enums
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.member.CreateProfileUseCase
import com.i2asolutions.athelo.useCase.member.LoadStoredUserMailUseCase
import com.i2asolutions.athelo.useCase.member.StoreUserEmailUseCase
import com.i2asolutions.athelo.useCase.member.StoreUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AdditionalInfoViewModel @Inject constructor(
    private val enums: Enums,
    private val loadStoredUserMailUseCase: LoadStoredUserMailUseCase,
    private val createProfileUseCase: CreateProfileUseCase,
    private val storeUserUseCase: StoreUserUseCase,
    private val storedUserMailUseCase: StoreUserEmailUseCase,
) : BaseViewModel<AdditionalInfoEvent, AdditionalInfoEffect>() {
    private var selectedUserTypeId: String = EnumItem.EMPTY.id
    private var displayName: String = ""
    private var currentState =
        AdditionalInfoViewState(enableButton = validate(), userTypes = enums.userTypes)
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
            is AdditionalInfoEvent.InputValueChanged -> handleInput(event.inputType)
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
                _effect.emit(AdditionalInfoEffect.ShowConnectSmartWatchScreen)
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
        return selectedUserTypeId.isNotBlank() && validateDisplayName() && ::userEmail.isInitialized
    }

    private fun validateDisplayName() = displayName.isNotBlank()
}