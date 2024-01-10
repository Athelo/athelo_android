package com.athelohealth.mobile.presentation.ui.caregiver.myWard

import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.chat.FindOrCreateConversationIdUseCase
import com.athelohealth.mobile.useCase.patients.DeletePatientUseCase
import com.athelohealth.mobile.useCase.patients.LoadPatientsUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.athelohealth.mobile.utils.consts.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyWardViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val deletePatient: DeletePatientUseCase,
    private val findOrCreateChat: FindOrCreateConversationIdUseCase,
) : BaseViewModel<MyWardEvent, MyWardEffect, MyWardViewState>(MyWardViewState()) {
    private val data = mutableSetOf<Patient>()
    private var nextUrl: String? = null

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            if (nextUrl.isNullOrBlank()) {
                data.clear()
            }
            val result = loadPatients(nextUrl)
            nextUrl = result.next
            if (result.result.isNotEmpty()) {
                data.addAll(result.result)
            }
            if (data.isEmpty()) {
                appManager.changeAppType(AppType.Unknown)
                notifyEffectChanged(MyWardEffect.ShowSelectRoleScreen)
            }
            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    patients = data.toList()
                )
            )
        }
    }

    override fun handleError(throwable: Throwable) {
        notifyStateChange(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun handleEvent(event: MyWardEvent) {
        when (event) {
            MyWardEvent.BackButtonClick -> notifyEffectChanged(MyWardEffect.ShowPrevScreen)
            is MyWardEvent.PatientClick -> {}
            is MyWardEvent.SendMessageClick -> {
                notifyStateChange(
                    currentState.copy(
                        isLoading = true,
                        showPatientMoreOption = null,
                        showPatientDeleteConfirmation = null
                    )
                )
                launchRequest {
                    val conversationId = findOrCreateChat(event.patient.userId)
                    notifyStateChange(currentState.copy(isLoading = false))
                    if (conversationId != null) {
                        notifyEffectChanged(
                            MyWardEffect.ShowPatientConversationScreen(
                                conversationId
                            )
                        )
                    } else {
                        errorMessage(Const.UNIVERSAL_ERROR_MESSAGE)
                    }
                }
            }
            MyWardEvent.CancelClick -> notifyStateChange(
                currentState.copy(
                    showPatientMoreOption = null,
                    showPatientDeleteConfirmation = null
                )
            )
            is MyWardEvent.DeleteWardClick -> {
                notifyStateChange(
                    currentState.copy(
                        showPatientMoreOption = null,
                        showPatientDeleteConfirmation = event.patient
                    )
                )
            }
            is MyWardEvent.ShowMoreOptionClick -> notifyStateChange(
                currentState.copy(
                    showPatientMoreOption = event.patient
                )
            )
            is MyWardEvent.DeleteWardConfirmationClick -> {
                notifyStateChange(
                    currentState.copy(
                        showPatientMoreOption = null,
                        showPatientDeleteConfirmation = null
                    )
                )
                launchRequest {
                    if (deletePatient(event.patient.userId)) {
                        nextUrl = null
                        loadData()
                    }
                }
            }
            MyWardEvent.AddPatientButtonClick -> notifyEffectChanged(MyWardEffect.ShowInvitationScreen)
        }
    }

}