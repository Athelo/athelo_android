package com.athelohealth.mobile.presentation.ui.share.steps

import com.athelohealth.mobile.extensions.*
import com.athelohealth.mobile.presentation.model.activity.ActivityGraphScreen
import com.athelohealth.mobile.presentation.model.chart.BarChartDataSet
import com.athelohealth.mobile.presentation.model.chart.BarChartEntry
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.HistoryRange
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen
import com.athelohealth.mobile.presentation.model.step.StepChartEntry
import com.athelohealth.mobile.presentation.model.step.StepEntry
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadStepEntriesForDataRangeUseCase
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
class StepsViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val loadStepEntriesForDataRange: LoadStepEntriesForDataRangeUseCase
) : BaseViewModel<StepsEvent, StepsEffect>() {

    private var currentState = StepsViewState(false)
    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private var datePeriod: Pair<Date, Date> = getDayPeriod()
    private var dateRange: HistoryRange = HistoryRange.Day

    private val stepsDesc = "" //todo - not returned from WS for now, so hiding it.
    private var selectedPatient: Patient? = null
    private var patients = mutableSetOf<Patient>()
    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun loadData() {
        launchRequest {
            if (appManager.appType.value is AppType.Caregiver) {
                loadPatients()
                selectPatient(appManager.appType.value.patientId)
            } else {
                selectedPatient = null
                patients.clear()
            }
            notifyStateChanged(currentState.copy(desc = stepsDesc))
            submitNewPeriodAndRange()
            loadDataForCurrentRangeAndPeriod()
        }
    }

    override fun handleEvent(event: StepsEvent) {
        when (event) {
            StepsEvent.BackClick -> notifyEffectChanged(StepsEffect.GoBack)
            StepsEvent.RefreshData -> launchRequest {
                loadDataForCurrentRangeAndPeriod()
            }
            StepsEvent.NextClicked -> launchRequest {
                loadNewPeriod(1)
            }
            StepsEvent.PrevClicked -> launchRequest {
                loadNewPeriod(-1)
            }
            is StepsEvent.RangeChanged -> launchRequest {
                changeRange(event.range)
            }
            is StepsEvent.ChangePatient -> launchRequest {
                selectPatient(event.patient.userId)
                submitNewPeriodAndRange()
                loadDataForCurrentRangeAndPeriod()
            }
        }
    }

    private fun notifyStateChanged(newState: StepsViewState) {
        currentState = newState
        launchOnUI { _viewState.emit(currentState) }
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
        notifyStateChanged(currentState.copy(selectedRange = dateRange, periodInfo = periodInfo))
    }

    private suspend fun loadDataForCurrentRangeAndPeriod() {
        notifyStateChanged(currentState.copy(isLoading = true))
        val patientId: Int? = selectedPatient?.userId?.toIntOrNull()
        val entries = runCatching {
            loadStepEntriesForDataRange(
                startDate = datePeriod.first,
                endDate = datePeriod.second,
                intervalFunction = if (dateRange == HistoryRange.Day) "HOUR" else "DAY",
                patientId = patientId,
            )
        }.onFailure { handleError(it) }.getOrElse { emptyList() }
        sendEntriesToUi(entries)
    }

    private fun sendEntriesToUi(entries: List<StepEntry>) {
        when (dateRange) {
            HistoryRange.Day -> sendDayViewToUi(entries)
            HistoryRange.Week -> sendWeekViewToUi(entries)
            HistoryRange.Month -> sendMonthViewToUi(entries)
        }
    }

    private fun sendDayViewToUi(entries: List<StepEntry>) {
        val sumSteps = "%d steps".format(entries.sumOf { it.value })
        val maxSteps = entries.map { it.value }.maxOrNull() ?: 0
        val yAxiStep = if (maxSteps < 1000) 250 else if (maxSteps < 5000) 1000 else 5000
        val customMaxValue = (maxSteps / yAxiStep + 1) * yAxiStep + 100f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillHourGaps()
            .map { StepChartEntry(it.key, it.value.firstOrNull()) }

        val dailyInfo = ActivityGraphScreen.ActivityInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("h a", Locale.US)
                        .format((it as? StepChartEntry)?.date ?: Date())
                },
                valueFormatter = { "" },
                cloudFormatter = ::getCloudDataForDay,
                xLabelStep = 4,
                yLabelStep = yAxiStep,
                customMaxValue = customMaxValue,
                drawYLines = true,
                hourMode = true,
            ),
            sumSteps
        )
        notifyStateChanged(currentState.copy(isLoading = false, information = dailyInfo))
    }

    private fun sendWeekViewToUi(entries: List<StepEntry>) {
        val avgSteps = "%d steps".format(entries.map { it.value }.average().toInt())
        val maxSteps = entries.map { it.value }.maxOrNull() ?: 0
        val yAxiStep = if (maxSteps < 1000) 250 else if (maxSteps < 5000) 1000 else 5000
        val customMaxValue = (maxSteps / yAxiStep + 1) * yAxiStep + 100f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map { StepChartEntry(it.key, it.value.firstOrNull()) }

        val weeklyInfo = ActivityGraphScreen.ActivityInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("EE", Locale.US)
                        .format((it as? StepChartEntry)?.date ?: Date())
                },
                valueFormatter = { "" },
                cloudFormatter = ::getCloudDataForWeek,
                yLabelStep = yAxiStep,
                customMaxValue = customMaxValue,
                drawYLines = true,
                barRatio = 0.3f,
            ),
            avgSteps
        )
        notifyStateChanged(currentState.copy(isLoading = false, information = weeklyInfo))
    }

    private fun sendMonthViewToUi(entries: List<StepEntry>) {
        val avgSteps = "%d steps".format(entries.map { it.value }.average().toInt())
        val maxSteps = entries.map { it.value }.maxOrNull() ?: 0
        val yAxiStep = if (maxSteps < 1000) 250 else if (maxSteps < 5000) 1000 else 5000
        val customMaxValue = (maxSteps / yAxiStep + 1) * yAxiStep + 100f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map { StepChartEntry(it.key, it.value.firstOrNull()) }

        val monthlyInfo = ActivityGraphScreen.ActivityInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("d", Locale.US)
                        .format((it as? StepChartEntry)?.date ?: Date())
                },
                valueFormatter = { "" },
                cloudFormatter = ::getCloudDataForMonth,
                xLabelStep = 5,
                yLabelStep = yAxiStep,
                customMaxValue = customMaxValue,
                drawYLines = true,
            ),
            avgSteps
        )
        notifyStateChanged(currentState.copy(isLoading = false, information = monthlyInfo))

    }

    private fun getCloudDataForMonth(entry: BarChartEntry): Pair<String, String> {
        val date = (entry as? StepChartEntry)?.date ?: Date()
        return "%d steps".format(entry.total.toInt()) to
                SimpleDateFormat("d MMMM", Locale.getDefault()).format(date)
    }

    private fun getCloudDataForWeek(entry: BarChartEntry): Pair<String, String> {
        val date = (entry as? StepChartEntry)?.date ?: Date()
        return "%d steps".format(entry.total.toInt()) to
                SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
    }

    private fun getCloudDataForDay(entry: BarChartEntry): Pair<String, String> {
        val date = (entry as? StepChartEntry)?.date ?: Date()
        return "%d steps".format(entry.total.toInt()) to
                SimpleDateFormat("h a", Locale.getDefault()).format(date)
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
        if (patients.isEmpty()) {
            notifyEffectChanged(StepsEffect.ShowLostCaregiverScreen)
        }
    }

    private fun selectPatient(patientId: String?) {
        val patient = patients.firstOrNull { it.userId == patientId } ?: patients.firstOrNull()
        selectedPatient = patient
        appManager.changePatientId(patient?.userId)
        notifyStateChanged(
            currentState.copy(selectedPatient = selectedPatient, patients = patients.toList())
        )
    }
}