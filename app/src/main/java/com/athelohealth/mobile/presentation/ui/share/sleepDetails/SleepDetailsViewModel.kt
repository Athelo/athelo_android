package com.athelohealth.mobile.presentation.ui.share.sleepDetails

import com.athelohealth.mobile.extensions.*
import com.athelohealth.mobile.presentation.model.chart.BarChartDataSet
import com.athelohealth.mobile.presentation.model.chart.BarChartEntry
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.*
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.presentation.ui.share.authorization.signInWithEmail.SignInWithEmailViewState
import com.athelohealth.mobile.useCase.health.LoadSleepEntriesForDataRangeUseCase
import com.athelohealth.mobile.useCase.patients.LoadPatientsUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.athelohealth.mobile.utils.app.patientId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SleepDetailsViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val loadSleepEntriesForDataRange: LoadSleepEntriesForDataRangeUseCase,
) : BaseViewModel<SleepDetailEvent, SleepDetailEffect, SleepDetailViewState>(SleepDetailViewState(false)) {
    private var datePeriod: Pair<Date, Date> = getDayPeriod()
    private var dateRange: HistoryRange = HistoryRange.Day

    private val sleepDesc = "" //todo - not returned from WS for now, so hiding it.
    private var selectedPatient: Patient? = null
    private var patients = mutableSetOf<Patient>()

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        notifyStateChange(currentState.copy(sleepDesc = sleepDesc))
        launchRequest {
            if (appManager.appType.value is AppType.Caregiver) {
                loadPatients()
                selectPatient(appManager.appType.value.patientId)
            } else {
                selectedPatient = null
                patients.clear()
            }
            submitNewPeriodAndRange()
            loadDataForCurrentRangeAndPeriod()
        }
    }

    override fun handleError(throwable: Throwable) {
        notifyStateChange(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun handleEvent(event: SleepDetailEvent) {
        when (event) {
            SleepDetailEvent.BackClick -> notifyEffectChanged(SleepDetailEffect.GoBack)
            SleepDetailEvent.RefreshData -> {
                launchRequest {
                    loadDataForCurrentRangeAndPeriod()
                }
            }
            SleepDetailEvent.NextClicked -> {
               launchRequest { loadNewPeriod(1) }
            }
            SleepDetailEvent.PrevClicked -> {
                launchRequest { loadNewPeriod(-1) }
            }
            is SleepDetailEvent.RangeChanged -> {
                launchRequest {
                    changeRange(event.range)
                }
            }
            is SleepDetailEvent.ChangePatient -> launchRequest {
                selectPatient(event.patient.userId)
                submitNewPeriodAndRange()
                loadDataForCurrentRangeAndPeriod()
            }
        }
    }

    private suspend fun loadNewPeriod(diff: Int) {
        if ((diff > 0 && !datePeriod.canMoveForward()) || (diff < 0 && !canMoveBackward())) return
        datePeriod = when (dateRange) {
            HistoryRange.Day -> getDayPeriod(datePeriod, diff)
            HistoryRange.Week -> getWeekPeriod(datePeriod, diff)
            HistoryRange.Month -> getMonthPeriod(datePeriod, diff)
        }
        submitNewPeriodAndRange()
        loadDataForCurrentRangeAndPeriod()
    }

    private suspend fun changeRange(newRange: HistoryRange) {
        val newPeriod = when (newRange) {
            HistoryRange.Day -> getDayPeriod()
            HistoryRange.Week -> getWeekPeriod()
            HistoryRange.Month -> getMonthPeriod()
        }
        datePeriod = newPeriod
        dateRange = newRange
        submitNewPeriodAndRange()
        loadDataForCurrentRangeAndPeriod()
    }

    private fun submitNewPeriodAndRange() {
        val periodInfo = SleepDetailScreen.PeriodInfo(
            datePeriod.getFormattedDate(dateRange),
            datePeriod.getRangeName(dateRange),
            canMoveBackward(),
            datePeriod.canMoveForward()
        )
        notifyStateChange(currentState.copy(selectedRange = dateRange, periodInfo = periodInfo))
    }

    private suspend fun loadDataForCurrentRangeAndPeriod() {
        notifyStateChange(currentState.copy(isLoading = true))
        val entries = runCatching {
            loadSleepEntriesForDataRange(
                startDate = datePeriod.first,
                endDate = datePeriod.second,
                patientId = selectedPatient?.userId?.toIntOrNull()
            )
        }.onFailure { handleError(it) }.getOrElse {
            List(
                when (dateRange) {
                    HistoryRange.Day -> 1
                    HistoryRange.Week -> 7
                    HistoryRange.Month -> 30
                }
            ) {
                SleepEntry(Date(), 0, SleepLevel.Awake)
            }
        }
        sendSleepEntriesToUi(entries)
    }

    private fun sendSleepEntriesToUi(entries: List<SleepEntry>) {
        when (dateRange) {
            HistoryRange.Day -> sendDayViewToUi(entries)
            HistoryRange.Week -> sendWeekViewToUi(entries)
            HistoryRange.Month -> sendMonthViewToUi(entries)
        }
    }

    private fun sendDayViewToUi(entries: List<SleepEntry>) {
        val dailyInfo = SleepDetailScreen.DailyInformation(
            entries.firstOrNull { it.level == SleepLevel.Deep }?.duration.displaySecsAsTime("0h 0m"),
            entries.firstOrNull { it.level == SleepLevel.Rem }?.duration.displaySecsAsTime("0h 0m"),
            entries.firstOrNull { it.level == SleepLevel.Light }?.duration.displaySecsAsTime("0h 0m"),
            entries.firstOrNull { it.level == SleepLevel.Awake }?.duration.displaySecsAsTime("0h 0m"),
            entries.sumOf { it.duration }.displaySecsAsTime(),
        )
        notifyStateChange(currentState.copy(isLoading = false, dailyInformation = dailyInfo))
    }

    private fun sendWeekViewToUi(entries: List<SleepEntry>) {
        val avgSleepTime = entries
            .groupBy { it.date }
            .map { it.value.sumOf { it2 -> it2.duration } }
            .average().toInt().displaySecsAsTime(fallback = "0m")

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map { SleepChartEntry(it.key, it.value) }

        val weeklyInfo = SleepDetailScreen.WeeklyInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("EE", Locale.US)
                        .format((it as? SleepChartEntry)?.date ?: Date())
                },
                valueFormatter = { "%.1f".format(it.value) },
                cloudFormatter = ::getCloudDataForEntry
            ),
            avgSleepTime
        )
        notifyStateChange(currentState.copy(isLoading = false, weeklyInformation = weeklyInfo))

    }

    private fun sendMonthViewToUi(entries: List<SleepEntry>) {
        val avgSleepTime = entries
            .groupBy { it.date }
            .map { it.value.sumOf { it2 -> it2.duration } }
            .average().toInt().displaySecsAsTime(fallback = "0m")
        val totalSleepTime = entries.sumOf { it.duration }
        val deepSleepTime = "%d%%".format(((entries.filter { it.level == SleepLevel.Deep }
            .sumOf { it.duration }.toFloat() / totalSleepTime) * 100)
            .toInt()
        )
        val remSleepTime = "%d%%".format(((entries.filter { it.level == SleepLevel.Rem }
            .sumOf { it.duration }.toFloat() / totalSleepTime) * 100)
            .toInt()
        )
        val lightSleepTime = "%d%%".format(((entries.filter { it.level == SleepLevel.Light }
            .sumOf { it.duration }.toFloat() / totalSleepTime) * 100)
            .toInt()
        )


        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map { SleepMonthChartEntry(it.key, it.value.sumOf { it2 -> it2.duration }) }

        val monthlyInfo = SleepDetailScreen.MonthlyInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("d", Locale.US)
                        .format((it as? SleepMonthChartEntry)?.date ?: Date())
                },
                valueFormatter = { "%.1f".format(it.value) },
                cloudFormatter = ::getCloudDataForEntry,
                xLabelStep = 5,
                barRatio = 0.8f
            ),
            avgSleepTime, deepSleepTime, remSleepTime, lightSleepTime
        )
        notifyStateChange(currentState.copy(isLoading = false, monthlyInformation = monthlyInfo))

    }

    private fun getCloudDataForEntry(sleepEntry: BarChartEntry): Pair<String, String> {
        val date = (sleepEntry as? SleepChartEntry)?.date
            ?: (sleepEntry as? SleepMonthChartEntry)?.date
            ?: Date()
        val secs = (sleepEntry.total * 60 * 60).toInt().displaySecsAsTime()
        return secs to SimpleDateFormat("d MMM", Locale.getDefault()).format(date)
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
        if(patients.isEmpty()){
            notifyEffectChanged(SleepDetailEffect.ShowLostCaregiverScreen)
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