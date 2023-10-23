package com.i2asolutions.athelo.presentation.ui.share.editProfile

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.model.enums.Enums
import com.i2asolutions.athelo.presentation.model.member.IdentityType
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.common.UploadImageUseCase
import com.i2asolutions.athelo.useCase.member.*
import com.i2asolutions.athelo.utils.AuthorizationException
import com.i2asolutions.athelo.utils.consts.Const.UNIVERSAL_ERROR_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val enums: Enums,
    private val storeUser: StoreUserUseCase,
    private val loadMyProfileUseCase: LoadMyProfileUseCase,
    private val updateUser: UpdateProfileUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val uploadImageProfileUseCase: UpdateProfileImageUseCase,
    private val sendForgotPasswordRequestUseCase: SendForgotPasswordRequestUseCase,
    private val checkAuthorizationIdentity: CheckAuthorizationIdentityUseCase,
) : BaseViewModel<EditProfileEvent, EditProfileEffect>() {
    private lateinit var originalUser: User
    private var currentState = EditProfileViewState(
        userTypes = enums.userTypes.filter { it.id != "3" },
        editMode = !EditProfileFragmentArgs.fromSavedStateHandle(savedStateHandle).enableEditMode,
        enableEditMode = EditProfileFragmentArgs.fromSavedStateHandle(savedStateHandle).enableEditMode,
    )
    private var phoneNumber = ""
    private var birthdate: String? = null
    private var userType = EnumItem.EMPTY
    private var newImage: Uri? = null
    private var displayName: String? = null

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {
        notifyStateChanged(currentState.copy(isLoading = true))
        launchRequest {
            originalUser = loadMyProfileUseCase() ?: throw AuthorizationException("Expire Session")
            phoneNumber = originalUser.phone ?: ""
            birthdate = originalUser.birthday
            userType = enums.userTypes.firstOrNull { it.id == originalUser.userType.toString() }
                ?: EnumItem.EMPTY
            storeUser(originalUser)
            displayName = originalUser.displayName
            val showRequestPassword =
                checkAuthorizationIdentity().any { it.type == IdentityType.Native }
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    user = originalUser,
                    selectedUserType = userType,
                    selectedBirthdate = birthdate,
                    hideRequestPasswordButton = !showRequestPassword,
                )
            )
        }
    }

    override fun handleEvent(event: EditProfileEvent) {
        when (event) {
            EditProfileEvent.EditButtonClick -> notifyStateChanged(currentState.copy(editMode = true))
            is EditProfileEvent.ImageSelected -> launchRequest {
                newImage = event.image
                notifyStateChanged(currentState.copy(isLoading = false, tmpUserImage = newImage))
            }
            is EditProfileEvent.InputChanged -> handleInputChange(event.inputType)
            EditProfileEvent.RequestPasswordResetClick -> originalUser.email?.let {
                launchRequest {
                    val result = sendForgotPasswordRequestUseCase(it)
                    withContext(Dispatchers.Main) {
                        successMessage(result.detail)
                    }
                }
            }
            EditProfileEvent.SaveButtonClick -> updateProfile()
            EditProfileEvent.UploadPictureClick -> notifyEffectChanged(EditProfileEffect.ShowImagePicker)
            EditProfileEvent.BackButtonClick -> notifyEffectChanged(EditProfileEffect.ShowPrevScreen)
            EditProfileEvent.HideProgress -> notifyStateChanged(currentState.copy(isLoading = false))
            EditProfileEvent.ShowProgress -> notifyStateChanged(currentState.copy(isLoading = true))
        }
    }

    private fun updateProfile() {
        if (!validate()) {
            errorMessage("Name field can not be empty.")
            return
        }
        notifyStateChanged(currentState.copy(isLoading = true))
        launchRequest {
            val userId = originalUser.id ?: throw NullPointerException(UNIVERSAL_ERROR_MESSAGE)
            val newId = newImage?.let { uri -> uploadImageUseCase(uri) }
            newId?.let { imageId -> uploadImageProfileUseCase(userId, imageId) }
            updateUser(
                userId,
                phoneNumber,
                birthdate,
                originalUser.email ?: "",
                displayName ?: originalUser.displayName ?: "",
                userType.id
            )
            clearCurrentValues()
            currentState = currentState.copy(
                enableSaveButton = validate(),
                tmpUserImage = null,
                editMode = !currentState.enableEditMode,
            )
            loadData()
        }
    }

    private fun clearCurrentValues() {
        phoneNumber = ""
        birthdate = null
        userType = EnumItem.EMPTY
        newImage = null
        displayName = null
    }

    private fun handleInputChange(inputType: InputType) {
        when (inputType) {
            is InputType.PhoneNumber -> {
                phoneNumber = inputType.value
            }
            is InputType.DropDown -> {
                userType =
                    enums.userTypes.firstOrNull { it.id == inputType.value } ?: EnumItem.EMPTY
            }
            is InputType.Calendar -> {
                birthdate = inputType.value
            }
            is InputType.PersonName -> {
                displayName = inputType.value
            }
            else -> {/*Ignore other  types */
            }
        }
        val user = currentState.user.copy(
            phone = phoneNumber,
            userType = userType.id.toInt(),
            birthday = birthdate,
            displayName = displayName
        )
        notifyStateChanged(
            currentState.copy(
                enableSaveButton = validate(),
                user = user,
                selectedUserType = userType,
                selectedBirthdate = birthdate
            )
        )
    }

    private fun validate(): Boolean = !displayName.isNullOrBlank()

    private fun notifyStateChanged(newState: EditProfileViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}