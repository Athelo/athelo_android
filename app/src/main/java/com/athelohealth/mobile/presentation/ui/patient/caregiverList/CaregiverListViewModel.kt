package com.athelohealth.mobile.presentation.ui.patient.caregiverList

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.model.caregiver.Caregiver
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.caregiver.DeleteCaregiverUseCase
import com.athelohealth.mobile.useCase.caregiver.LoadCaregiversUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CaregiverListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appManager: AppManager,
    private val loadCaregivers: LoadCaregiversUseCase,
    private val removeCaregiver: DeleteCaregiverUseCase,
    private val loadProfile: LoadMyProfileUseCase,
) : BaseViewModel<CaregiverListEvent, CaregiverListEffect, CaregiverListViewState>(CaregiverListViewState()) {
    private var caregivers = mutableSetOf<Caregiver>()
    private val initialFlow =
        CaregiverListFragmentArgs.fromSavedStateHandle(savedStateHandle).initialFlow

    private var nextUrl: String? = null

    init {
        loadList()
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {}

    private fun loadList() {
        showLoading()
        launchRequest {
            if (nextUrl == null) {
                caregivers.clear()
            }
            val result = loadCaregivers(nextUrl)
            nextUrl = result.next
            if (result.result.isNotEmpty()) {
                caregivers.addAll(result.result)
            }
            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    myCaregivers = caregivers.toList()
                )
            )
        }
    }

    private fun showLoading() {
        notifyStateChange(currentState.copy(isLoading = true))
    }

    override fun handleEvent(event: CaregiverListEvent) {
        when (event) {
            CaregiverListEvent.AddCaregiverClick -> notifyEffectChanged(CaregiverListEffect.ShowInvitationScreen)
            CaregiverListEvent.BackButtonClick -> notifyEffectChanged(CaregiverListEffect.ShowPrevScreen)
            is CaregiverListEvent.SelectCaregiverClick -> {
                notifyStateChange(currentState.copy(showCaregiverDeleteConfirmation = event.caregiver))
            }
            is CaregiverListEvent.ConfirmationDeleteClick -> sendDeleteRequest(event.caregiver)
            CaregiverListEvent.ProceedClick -> launchRequest {
                appManager.changeAppType(AppType.Patient)
                val profile = loadProfile()
                val hasFitbitConnected = profile?.fitBitConnected ?: false
                notifyEffectChanged(if (initialFlow && !hasFitbitConnected) CaregiverListEffect.ShowSmartWatchScreen else CaregiverListEffect.ShowHomeScreen)
            }
            CaregiverListEvent.CancelClick -> notifyStateChange(
                currentState.copy(
                    showCaregiverDeleteConfirmation = null
                )
            )
        }
    }

    private fun sendDeleteRequest(caregiver: Caregiver) {
        showLoading()
        launchRequest {
            if (removeCaregiver(caregiverId = caregiver.id)) {
                nextUrl = null
            }
            loadData()
        }
    }
}