package com.athelohealth.mobile.presentation.ui.share.activity

import com.athelohealth.mobile.extensions.fillGaps
import com.athelohealth.mobile.extensions.getLastWeekPeriod
import com.athelohealth.mobile.presentation.model.activity.ActivityScreen
import com.athelohealth.mobile.presentation.model.exercise.ExerciseEntry
import com.athelohealth.mobile.presentation.model.heartRate.HeartRateEntry
import com.athelohealth.mobile.presentation.model.hrv.HrvEntry
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.step.StepEntry
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.presentation.ui.share.authorization.signInWithEmail.SignInWithEmailViewState
import com.athelohealth.mobile.useCase.health.LoadExerciseForDataRangeUseCase
import com.athelohealth.mobile.useCase.health.LoadHeartRateEntriesForDataRangeUseCase
import com.athelohealth.mobile.useCase.health.LoadHrvForDataRangeUseCase
import com.athelohealth.mobile.useCase.health.LoadStepEntriesForDataRangeUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.useCase.patients.LoadPatientsUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.athelohealth.mobile.utils.app.patientId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val loadMyProfile: LoadMyProfileUseCase,
    private val storeUser: StoreUserUseCase,
    private val loadStepEntriesForDataRange: LoadStepEntriesForDataRangeUseCase,
    private val loadExerciseForDataRange: LoadExerciseForDataRangeUseCase,
    private val loadHeartRateEntriesForDataRange: LoadHeartRateEntriesForDataRangeUseCase,
    private val loadHrvForDataRange: LoadHrvForDataRangeUseCase,
) : BaseViewModel<ActivityEvent, ActivityEffect, ActivityViewState>(ActivityViewState(false)) {

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    private var datePeriod: Pair<Date, Date> = getLastWeekPeriod()

    private val topDesc = ""//todo - not returned from WS for now, so hiding it.

    private lateinit var user: User
    private var selectedPatient: Patient? = null
    private var patients = mutableSetOf<Patient>()


    override fun loadData() {
        launchRequest {
            if (appManager.appType.value is AppType.Caregiver) {
                loadPatients()
                selectPatient( appManager.appType.value.patientId)
                loadDataForCurrentPeriodForPatient(appManager.appType.value.patientId?.toIntOrNull())
            } else {
                selectedPatient = null
                patients.clear()
                notifyStateChange(
                    currentState.copy(
                        topHint = topDesc,
                        patients = emptyList(),
                        selectedPatient = null
                    )
                )
                loadDataForCurrentPeriod()
            }
        }
    }

    override fun handleEvent(event: ActivityEvent) {
        when (event) {
            ActivityEvent.MenuClick -> notifyEffectChanged(ActivityEffect.ShowMenu)
            ActivityEvent.RefreshData -> if (appManager.appType.value is AppType.Patient) loadDataForCurrentPeriod() else launchRequest {
                loadDataForCurrentPeriodForPatient(
                    selectedPatient?.userId?.toIntOrNull()
                        ?: appManager.appType.value.patientId?.toIntOrNull()
                )
            }
            ActivityEvent.ConnectSmartWatchClick -> notifyEffectChanged(ActivityEffect.ShowConnectSmartWatchScreen)
            ActivityEvent.MyProfileClick -> notifyEffectChanged(ActivityEffect.ShowMyProfileScreen)
            ActivityEvent.StepsClick -> notifyEffectChanged(ActivityEffect.ShowStepsScreen)
            ActivityEvent.ActivityClick -> notifyEffectChanged(ActivityEffect.ShowActivityScreen)
            ActivityEvent.HeartRateClick -> notifyEffectChanged(ActivityEffect.ShowHeartRateScreen)
            ActivityEvent.HrvClick -> notifyEffectChanged(ActivityEffect.ShowHrvScreen)
            is ActivityEvent.ChangePatient -> launchRequest {
                selectPatient(event.patient.userId)
                loadDataForCurrentPeriodForPatient(event.patient.userId.toIntOrNull())
            }
        }
    }

    private suspend fun loadPatients() {
        patients.clear()
        var nextUrl: String? = null
        var loadNext = true
        while (loadNext) {
            val result = loadPatients(nextUrl)
            nextUrl = result.next
            loadNext = !nextUrl.isNullOrBlank()
            if (result.result.isNotEmpty())
                patients.addAll(result.result)
        }
    }

    private fun selectPatient(patientId: String?) {
        val patient = patients.firstOrNull { it.userId == patientId } ?: patients.firstOrNull()
        selectedPatient = patient
        appManager.changePatientId(patient?.userId)
        notifyStateChange(
            currentState.copy(selectedPatient = selectedPatient, patients = patients.toList())
        )
    }

    override fun handleError(throwable: Throwable) {
        notifyStateChange(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    private suspend fun loadDataForCurrentPeriodForPatient(patientId: Int?) {
        notifyStateChange(currentState.copy(isLoading = true))
        user = loadMyProfile().also { storeUser(it) } ?: throw AuthorizationException()
        if (!user.isCaregiver) {
            notifyEffectChanged(ActivityEffect.ShowSelectRoleScreen)
            return
        }
        val steps = loadSteps(patientId = patientId)
        val exercise = loadExercises(patientId)
        val heartRate = loadHeartRate(patientId)
        val hrv = loadHrv()

        notifyStateChange(
            currentState.copy(
                isLoading = false,
                currentUser = user,
                stepsInformation = steps,
                activityInformation = exercise,
                heartRateInformation = heartRate,
                hrvInformation = hrv,
                patients = patients.toList(),
                selectedPatient = selectedPatient,
            )
        )
    }

    private fun loadDataForCurrentPeriod() {
        launchRequest {
            notifyStateChange(currentState.copy(isLoading = true))

            user = loadMyProfile().also { storeUser(it) } ?: throw AuthorizationException()
            val fitBitConnected = user.fitBitConnected
            notifyStateChange(
                currentState.copy(
                    isLoading = fitBitConnected,
                    showNotConnected = !fitBitConnected,
                    currentUser = user,
                    selectedPatient = null,
                )
            )
            if (!fitBitConnected) return@launchRequest

            val steps = loadSteps(null)
            val exercise = loadExercises(null)
            val heartRate = loadHeartRate(null)
            val hrv = loadHrv()

            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    stepsInformation = steps,
                    activityInformation = exercise,
                    heartRateInformation = heartRate,
                    hrvInformation = hrv,
                )
            )
        }
    }

    private suspend fun loadSteps(patientId: Int?): ActivityScreen.Steps {
        val stepEntries = runCatching {
            loadStepEntriesForDataRange(
                startDate = datePeriod.first,
                endDate = datePeriod.second,
                patientId = patientId,
            )
        }.onFailure { handleError(it) }.getOrElse { List(7) { StepEntry(Date(), 0) } }
        val data = stepEntries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
        val flatData = data.values
            .flatMap { listOf(if (it.isEmpty()) 0f else it.first().value.toFloat()) }
        val max = flatData.maxOf { it } * 1.1f
        return ActivityScreen.Steps((data[data.lastKey()]?.sumOf { it.value } ?: 0).formatSteps(),
            flatData.map { it / max })
    }

    private suspend fun loadExercises(patientId: Int?): ActivityScreen.Activity {
        val stepEntries = runCatching {
            loadExerciseForDataRange(
                startDate = datePeriod.first,
                endDate = datePeriod.second,
                patientId = patientId,
            )
        }.onFailure { handleError(it) }.getOrElse { List(7) { ExerciseEntry(Date(), 0) } }
        val data = stepEntries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
        val flatData = data.values
            .flatMap { listOf(if (it.isEmpty()) 0f else it.first().value.toFloat()) }
        val max = flatData.maxOf { it } + 1f
        return ActivityScreen.Activity("%d min".format((data[data.lastKey()]?.sumOf { it.value }
            ?: 0)),
            flatData.map { it / max })
    }

    private suspend fun loadHeartRate(patientId: Int?): ActivityScreen.HeartRate {
        val patientId: Int? = selectedPatient?.userId?.toIntOrNull()
        val stepEntries = runCatching {
            loadHeartRateEntriesForDataRange(
                startDate = datePeriod.first,
                endDate = datePeriod.second,
                patientId = patientId,
            )
        }.onFailure { handleError(it) }.getOrElse { List(7) { HeartRateEntry(Date(), 0) } }
        val data = stepEntries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
        val flatData = data.values
            .flatMap { listOf(if (it.isEmpty()) 0f else it.first().value.toFloat()) }
        val max = flatData.maxOf { it } + 1f
        return ActivityScreen.HeartRate("%d bps".format((data[data.lastKey()]?.sumOf { it.value }
            ?: 0)),
            flatData.map { it / max })
    }

    private suspend fun loadHrv(): ActivityScreen.HeartRateVariability {
        val patientId: Int? = selectedPatient?.userId?.toIntOrNull()
        val stepEntries = runCatching {
            loadHrvForDataRange(
                startDate = datePeriod.first,
                endDate = datePeriod.second,
                patientId = patientId,
            )
        }.onFailure { handleError(it) }.getOrElse { List(7) { HrvEntry(Date(), 0) } }
        val data = stepEntries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
        val flatData = data.values
            .flatMap { listOf(if (it.isEmpty()) 0f else it.first().value.toFloat()) }
        val max = flatData.maxOf { it } + 1f
        return ActivityScreen.HeartRateVariability("", flatData.map { it / max })
    }

    private fun Int.formatSteps(): String {
        val formatter = DecimalFormat.getInstance() as DecimalFormat
        formatter.decimalFormatSymbols =
            formatter.decimalFormatSymbols.apply { groupingSeparator = ' ' }

        return formatter.format(this)
    }

}