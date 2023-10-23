package com.i2asolutions.athelo.presentation.ui.caregiver.lostCaregiver

import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.utils.app.AppManager
import com.i2asolutions.athelo.utils.app.AppType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LostCaregiverViewModel @Inject constructor(
    private val appManager: AppManager,
) : BaseViewModel<LostCaregiverEvent, LostCaregiverEffect>() {
    private var currentState = LostCaregiverViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

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

    private fun notifyStateChanged(newState: LostCaregiverViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}