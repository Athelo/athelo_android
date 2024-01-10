package com.athelohealth.mobile.presentation.ui.share.editProfile

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.model.enums.Enums
import com.athelohealth.mobile.presentation.model.member.IdentityType
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.common.UploadImageUseCase
import com.athelohealth.mobile.useCase.member.*
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.consts.Const.UNIVERSAL_ERROR_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val appManager: AppManager
) : BaseViewModel<EditProfileEvent, EditProfileEffect, EditProfileViewState>(EditProfileViewState(
    userTypes = enums.userTypes.filter { it.id != "3" },
    editMode = false,
    enableEditMode = false,
)) {
    private lateinit var originalUser: User
    init {
        notifyStateChange(
            EditProfileViewState(
                userTypes = enums.userTypes.filter { it.id != "3" },
                editMode = !EditProfileFragmentArgs.fromSavedStateHandle(savedStateHandle).enableEditMode,
                enableEditMode = EditProfileFragmentArgs.fromSavedStateHandle(savedStateHandle).enableEditMode,
            )
        )
    }
    private var phoneNumber = ""
    private var birthdate: String? = null
    private var userType = EnumItem.EMPTY
    private var newImage: Uri? = null
    private var displayName: String? = null

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            originalUser = loadMyProfileUseCase() ?: throw AuthorizationException("Expire Session")
            phoneNumber = originalUser.phone ?: ""
            birthdate = originalUser.birthday
            userType = enums.userTypes.firstOrNull { it.id == originalUser.userType.toString() }
                ?: EnumItem.EMPTY
            storeUser(originalUser)
            displayName = originalUser.displayName
            val showRequestPassword = appManager.authenticationType == IdentityType.Native
            notifyStateChange(
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
            EditProfileEvent.EditButtonClick -> notifyStateChange(currentState.copy(editMode = true))
            is EditProfileEvent.ImageSelected -> launchRequest {
                newImage = event.image
                notifyStateChange(currentState.copy(isLoading = false, tmpUserImage = newImage))
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
            EditProfileEvent.HideProgress -> notifyStateChange(currentState.copy(isLoading = false))
            EditProfileEvent.ShowProgress -> notifyStateChange(currentState.copy(isLoading = true))
        }
    }

    private fun updateProfile() {
        if (!validate()) {
            errorMessage("Name field can not be empty.")
            return
        }
        notifyStateChange(currentState.copy(isLoading = true))
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
        notifyStateChange(
            currentState.copy(
                enableSaveButton = validate(),
                user = user,
                selectedUserType = userType,
                selectedBirthdate = birthdate
            )
        )
    }

    private fun validate(): Boolean = !displayName.isNullOrBlank()

}