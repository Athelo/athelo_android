package com.i2asolutions.athelo.presentation.ui.caregiver.patientList

import androidx.lifecycle.SavedStateHandle
import com.i2asolutions.athelo.presentation.model.patients.Patient
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.caregiver.LoadPatientsUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.utils.app.AppManager
import com.i2asolutions.athelo.utils.app.AppType
import com.i2asolutions.athelo.utils.app.patientId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PatientListViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val loadProfile: LoadMyProfileUseCase,
) : BaseViewModel<PatientListEvent, PatientListEffect>() {
    private var currentState = PatientListViewState()
    private val data = mutableSetOf<Patient>()
    private val initialFlow = PatientListFragmentArgs.fromSavedStateHandle(savedState).initialFlow
    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()
    private var nextUrl: String? = null
    private var selectedPatient: Patient? = null


    override fun loadData() {
        loadList()
    }

    override fun handleEvent(event: PatientListEvent) {
        when (event) {
            PatientListEvent.BackButtonClick -> notifyEffectChanged(PatientListEffect.ShowPrevScreen)
            PatientListEvent.AddPatientClick -> notifyEffectChanged(PatientListEffect.ShowInvitationCodeScreen)
            PatientListEvent.ProceedClick -> {
                selectedPatient?.let {
                    launchRequest {
                        appManager.changeAppType(AppType.Caregiver(it.userId))
                        val profile = loadProfile()
                        val fitbitConnected = profile?.fitBitConnected ?: false
                        notifyEffectChanged(if (initialFlow && !fitbitConnected) PatientListEffect.ShowSmartWatchScreen else PatientListEffect.ShowHomeScreen)
                    }
                } ?: errorMessage("You must choose patient before proceed.")
            }
            is PatientListEvent.PatientCellClick -> {
                if (selectedPatient != event.patient) {
                    selectedPatient?.selected = false
                    selectedPatient = event.patient
                    selectedPatient?.selected = true
                    notifyStateChanged(currentState.copy(selectedPatient = selectedPatient))
                }
            }
        }
    }

    private fun loadList() {
        notifyStateChanged(currentState.copy(isLoading = true))
        launchRequest {
            if (nextUrl.isNullOrBlank()) {
                data.clear()
            }
            val patients = loadPatients(nextUrl)
            nextUrl = patients.next
            if (patients.result.isNotEmpty()) {
                data.addAll(patients.result)
                selectedPatient =
                    (appManager.appType.value.patientId?.let { patientId -> data.firstOrNull { it.userId == patientId || it.userId == selectedPatient?.userId } }
                        ?: data.firstOrNull())?.also { it.selected = true }
            }
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    patientList = data.toList(),
                    selectedPatient = selectedPatient,
                    enableProceedButton = validate(),
                )
            )
        }
    }

    private fun validate(): Boolean = data.isNotEmpty() && selectedPatient != null

    private fun notifyStateChanged(newState: PatientListViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}