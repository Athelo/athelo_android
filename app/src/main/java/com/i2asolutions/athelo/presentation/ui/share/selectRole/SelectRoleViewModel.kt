package com.i2asolutions.athelo.presentation.ui.share.selectRole

import androidx.lifecycle.SavedStateHandle
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectRoleViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    BaseViewModel<SelectRoleEvent, SelectRoleEffect>() {
    private val initFlow = SelectRoleFragmentArgs.fromSavedStateHandle(savedStateHandle).initialFlow
    private var currentState = SelectRoleViewState(showBackButton = !initFlow)

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {

    }

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

    private fun notifyStateChanged(newState: SelectRoleViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}