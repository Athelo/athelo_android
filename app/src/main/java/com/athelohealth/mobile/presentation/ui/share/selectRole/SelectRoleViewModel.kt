package com.athelohealth.mobile.presentation.ui.share.selectRole

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectRoleViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    BaseViewModel<SelectRoleEvent, SelectRoleEffect, SelectRoleViewState>(SelectRoleViewState(showBackButton = false)) {
    private val initFlow = SelectRoleFragmentArgs.fromSavedStateHandle(savedStateHandle).initialFlow

    init {
        notifyStateChange(SelectRoleViewState(showBackButton = !initFlow))
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {}

    override fun handleEvent(event: SelectRoleEvent) {
        when (event) {
            SelectRoleEvent.ActAsCaregiverClick -> notifyEffectChanged(
                SelectRoleEffect.ShowPatientListScreen(
                    initFlow
                )
            )
            SelectRoleEvent.ActAsPatientClick -> notifyEffectChanged(
                SelectRoleEffect.ShowCaregiverListScreen(
                    initFlow
                )
            )
            SelectRoleEvent.BackButtonClick -> notifyEffectChanged(SelectRoleEffect.ShowPrevScreen)
        }
    }
}