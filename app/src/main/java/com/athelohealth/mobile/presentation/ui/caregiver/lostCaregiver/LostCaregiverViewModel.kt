package com.athelohealth.mobile.presentation.ui.caregiver.lostCaregiver

import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.presentation.ui.share.authorization.signInWithEmail.SignInWithEmailViewState
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LostCaregiverViewModel @Inject constructor(
    private val appManager: AppManager,
) : BaseViewModel<LostCaregiverEvent, LostCaregiverEffect, LostCaregiverViewState>(LostCaregiverViewState()) {

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        appManager.changeAppType(AppType.Unknown)
    }

    override fun handleEvent(event: LostCaregiverEvent) {
        when (event) {
            LostCaregiverEvent.OkButtonClick -> {
                notifyEffectChanged(LostCaregiverEffect.ShowRoleScreen)
            }
        }
    }
}