package com.i2asolutions.athelo.presentation.ui.caregiver.myWard

import com.i2asolutions.athelo.presentation.model.patients.Patient
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.chat.FindOrCreateConversationIdUseCase
import com.i2asolutions.athelo.useCase.patients.DeletePatientUseCase
import com.i2asolutions.athelo.useCase.patients.LoadPatientsUseCase
import com.i2asolutions.athelo.utils.app.AppManager
import com.i2asolutions.athelo.utils.app.AppType
import com.i2asolutions.athelo.utils.consts.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyWardViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val deletePatient: DeletePatientUseCase,
    private val findOrCreateChat: FindOrCreateConversationIdUseCase,
) : BaseViewModel<MyWardEvent, MyWardEffect>() {
    private val data = mutableSetOf<Patient>()
    private var currentState = MyWardViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    private var nextUrl: String? = null

    override fun loadData() {
        notifyStateChanged(currentState.copy(isLoading = true))
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
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    patients = data.toList()
                )
            )
        }
    }

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun handleEvent(event: MyWardEvent) {
        when (event) {
            MyWardEvent.BackButtonClick -> notifyEffectChanged(MyWardEffect.ShowPrevScreen)
            is MyWardEvent.PatientClick -> {}
            is MyWardEvent.SendMessageClick -> {
                notifyStateChanged(
                    currentState.copy(
                        isLoading = true,
                        showPatientMoreOption = null,
                        showPatientDeleteConfirmation = null
                    )
                )
                launchRequest {
                    val conversationId = findOrCreateChat(event.patient.userId)
                    notifyStateChanged(currentState.copy(isLoading = false))
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
            MyWardEvent.CancelClick -> notifyStateChanged(
                currentState.copy(
                    showPatientMoreOption = null,
                    showPatientDeleteConfirmation = null
                )
            )
            is MyWardEvent.DeleteWardClick -> {
                notifyStateChanged(
                    currentState.copy(
                        showPatientMoreOption = null,
                        showPatientDeleteConfirmation = event.patient
                    )
                )
            }
            is MyWardEvent.ShowMoreOptionClick -> notifyStateChanged(
                currentState.copy(
                    showPatientMoreOption = event.patient
                )
            )
            is MyWardEvent.DeleteWardConfirmationClick -> {
                notifyStateChanged(
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

    private fun notifyStateChanged(newState: MyWardViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}