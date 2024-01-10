package com.athelohealth.mobile.presentation.ui.share.sleepSummary

import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.displaySecsAsTime
import com.athelohealth.mobile.extensions.getWeekPeriod
import com.athelohealth.mobile.presentation.model.chart.CircleChartDataSet
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.SleepEntry
import com.athelohealth.mobile.presentation.model.sleep.SleepLevel
import com.athelohealth.mobile.presentation.model.sleep.SleepSummaryScreen.*
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.presentation.ui.theme.lightOlivaceous
import com.athelohealth.mobile.useCase.deviceConfig.LoadDeviceConfigUseCase
import com.athelohealth.mobile.useCase.health.LoadSleepEntriesForDataRangeUseCase
import com.athelohealth.mobile.useCase.health.LoadSleepEntriesForTodayUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.useCase.patients.LoadPatientsUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.athelohealth.mobile.utils.app.patientId
import com.athelohealth.mobile.utils.conectivity.NetWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SleepSummaryViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val loadMyProfile: LoadMyProfileUseCase,
    private val storeUser: StoreUserUseCase,
    private val loadDeviceConfig: LoadDeviceConfigUseCase,
    private val loadSleepEntriesForYesterday: LoadSleepEntriesForTodayUseCase,
    private val loadSleepEntriesForDataRange: LoadSleepEntriesForDataRangeUseCase,
) : BaseViewModel<SleepSummaryEvent, SleepEffect, SleepViewState>(SleepViewState(false)) {
    private lateinit var user: User
    private var selectedPatient: Patient? = null
    private var patients = mutableSetOf<Patient>()

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        if (NetWorkManager.isDisconnected) errorNoInternet()
        else if (appManager.appType.value is AppType.Caregiver) {
            launchRequest {
                loadPatients()
                selectPatient(appManager.appType.value.patientId)
                loadSleepSettingsForPatient(appManager.appType.value.patientId?.toIntOrNull())
            }
        } else {
            selectedPatient = null
            patients.clear()
            loadSleepSettings()
        }
    }

    override fun handleEvent(event: SleepSummaryEvent) {
        when (event) {
            SleepSummaryEvent.MenuClick -> notifyEffectChanged(SleepEffect.ShowMenuScreen)
            SleepSummaryEvent.ConnectSmartWatchClick -> {
                notifyEffectChanged(SleepEffect.ShowConnectSmartWatchScreen)
            }
            SleepSummaryEvent.MoreClick -> {
                notifyEffectChanged(SleepEffect.ShowSleepDetailsScreen)
            }
            SleepSummaryEvent.MyProfileClick -> {
                notifyEffectChanged(SleepEffect.ShowMyProfileScreen)
            }
            is SleepSummaryEvent.ReadArticleClick -> {
                notifyEffectChanged(SleepEffect.ShowArticle(event.articleId))
            }
            SleepSummaryEvent.RefreshData -> {
                loadSleepSettings()
            }
            is SleepSummaryEvent.ChangePatient -> launchRequest {
                selectPatient(event.patient.userId)
                loadSleepSettingsForPatient(event.patient.userId.toIntOrNull())
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        notifyStateChange(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    private fun loadSleepSettings() {
        launchRequest {
            notifyStateChange(currentState.copy(isLoading = true))
            user = loadMyProfile().also { storeUser(it) } ?: throw AuthorizationException()
            val fitBitConnected = user.fitBitConnected
            notifyStateChange(
                currentState.copy(
                    isLoading = fitBitConnected,
                    showNotConnected = !fitBitConnected,
                    currentUser = user
                )
            )
            if (!fitBitConnected) return@launchRequest
            val deviceConfig = loadDeviceConfig()
            val idealSleep =
                if (deviceConfig.idealSleepSecs != null && deviceConfig.idealSleepArticleId != null) {
                    IdealSleep(
                        time = deviceConfig.idealSleepSecsFormatted,
                        articleId = deviceConfig.idealSleepArticleId,
                        title = deviceConfig.idealSleepText ?: "",
                        image = R.drawable.ic_ideal_sleep
                    )
                } else null

            val lastWeekRange = getWeekPeriod(getWeekPeriod(), diff = -1)
            val lastWeekResults =
                runCatching {
                    loadSleepEntriesForDataRange(
                        startDate = lastWeekRange.first,
                        endDate = lastWeekRange.second,
                        patientId = null,
                    )
                }.onFailure { handleError(it) }.getOrElse {
                    List(7) {
                        SleepEntry(Date(), 0, SleepLevel.Awake)
                    }
                }
            val results = runCatching { loadSleepEntriesForYesterday() }.getOrElse {
                List(1) {
                    SleepEntry(Date(), 0, SleepLevel.Awake)
                }
            }
            val weekTotal = lastWeekResults
                .groupBy { it.date }
                .map { it.value.sumOf { it2 -> it2.duration } }
                .average()
                .toFloat()

            val sleepResult = if (deviceConfig.idealSleepSecs != null) {
                val value = if (lastWeekResults.isEmpty() || weekTotal == 0f) 0f
                else weekTotal / deviceConfig.idealSleepSecs

                SleepResult(
                    text = if (value == 0f) "No figures for this week" else "You almost reach a perfect week of sleep",
                    chartDataSet = CircleChartDataSet(
                        value = value,
                        bgColor = lightOlivaceous.copy(alpha = 0.2f),
                        textColor = lightOlivaceous,
                        valueColor = lightOlivaceous,
                        formattedValue = if (value == 0f) "No Data" else weekTotal.toInt()
                            .displaySecsAsTime()
                    )
                )
            } else null

            val sleepInformation = SleepInformation(
                deepSleep = results.firstOrNull { it.level == SleepLevel.Deep }?.duration
                    .displaySecsAsTime("0h 0m"),
                rem = results.firstOrNull { it.level == SleepLevel.Rem }?.duration
                    .displaySecsAsTime("0h 0m"),
                lightSleep = results.firstOrNull { it.level == SleepLevel.Light }?.duration
                    .displaySecsAsTime("0h 0m"),
                awake = results.firstOrNull { it.level == SleepLevel.Awake }?.duration
                    .displaySecsAsTime("0h 0m")
            )

            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    currentUser = user,
                    idealSleep = idealSleep,
                    sleepResult = sleepResult,
                    sleepInformation = sleepInformation
                )
            )
        }
    }

    private suspend fun loadSleepSettingsForPatient(patientId: Int?) {
        notifyStateChange(currentState.copy(isLoading = true))
        user = loadMyProfile().also { storeUser(it) } ?: throw AuthorizationException()
        if (!user.isCaregiver) {
            notifyEffectChanged(SleepEffect.ShowLostCaregiverScreen)
            return
        }
        val deviceConfig = loadDeviceConfig()
        val idealSleep =
            if (deviceConfig.idealSleepSecs != null && deviceConfig.idealSleepArticleId != null) {
                IdealSleep(
                    time = deviceConfig.idealSleepSecsFormatted,
                    articleId = deviceConfig.idealSleepArticleId,
                    title = deviceConfig.idealSleepText ?: "",
                    image = R.drawable.ic_ideal_sleep
                )
            } else null

        val lastWeekRange = getWeekPeriod(getWeekPeriod(), diff = -1)
        val lastWeekResults =
            runCatching {
                loadSleepEntriesForDataRange(
                    startDate = lastWeekRange.first,
                    endDate = lastWeekRange.second,
                    patientId = patientId,
                )
            }.getOrElse {
                List(7) {
                    SleepEntry(Date(), 0, SleepLevel.Awake)
                }
            }
        val results = runCatching { loadSleepEntriesForYesterday() }.getOrElse {
            List(1) {
                SleepEntry(Date(), 0, SleepLevel.Awake)
            }
        }
        val weekTotal = lastWeekResults
            .groupBy { it.date }
            .map { it.value.sumOf { it2 -> it2.duration } }
            .average()
            .toFloat()

        val sleepResult = if (deviceConfig.idealSleepSecs != null) {
            val value = if (lastWeekResults.isEmpty() || weekTotal == 0f) 0f
            else weekTotal / deviceConfig.idealSleepSecs

            SleepResult(
                text = if (value == 0f) "No figures for this week" else "You almost reach a perfect week of sleep",
                chartDataSet = CircleChartDataSet(
                    value = value,
                    bgColor = lightOlivaceous.copy(alpha = 0.2f),
                    textColor = lightOlivaceous,
                    valueColor = lightOlivaceous,
                    formattedValue = if (value == 0f) "No Data" else weekTotal.toInt()
                        .displaySecsAsTime()
                )
            )
        } else null

        val sleepInformation = SleepInformation(
            deepSleep = results.firstOrNull { it.level == SleepLevel.Deep }?.duration
                .displaySecsAsTime("0h 0m"),
            rem = results.firstOrNull { it.level == SleepLevel.Rem }?.duration
                .displaySecsAsTime("0h 0m"),
            lightSleep = results.firstOrNull { it.level == SleepLevel.Light }?.duration
                .displaySecsAsTime("0h 0m"),
            awake = results.firstOrNull { it.level == SleepLevel.Awake }?.duration
                .displaySecsAsTime("0h 0m")
        )

        notifyStateChange(
            currentState.copy(
                showNotConnected = false,
                isLoading = false,
                currentUser = user,
                idealSleep = idealSleep,
                sleepResult = sleepResult,
                sleepInformation = sleepInformation
            )
        )
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
}