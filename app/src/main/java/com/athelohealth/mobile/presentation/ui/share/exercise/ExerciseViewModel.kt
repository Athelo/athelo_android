package com.athelohealth.mobile.presentation.ui.share.exercise

import com.athelohealth.mobile.extensions.*
import com.athelohealth.mobile.presentation.model.activity.ActivityGraphScreen
import com.athelohealth.mobile.presentation.model.chart.BarChartDataSet
import com.athelohealth.mobile.presentation.model.chart.BarChartEntry
import com.athelohealth.mobile.presentation.model.exercise.ExerciseChartEntry
import com.athelohealth.mobile.presentation.model.exercise.ExerciseEntry
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.HistoryRange
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadExerciseForDataRangeUseCase
import com.athelohealth.mobile.useCase.patients.LoadPatientsUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.athelohealth.mobile.utils.app.patientId
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val loadExerciseForDataRange: LoadExerciseForDataRangeUseCase
) : BaseViewModel<ExerciseEvent, ExerciseEffect, ExerciseViewState>(ExerciseViewState(false)) {

    private var datePeriod: Pair<Date, Date> = getDayPeriod()
    private var dateRange: HistoryRange = HistoryRange.Day

    private val stepsDesc = "" //todo - not returned from WS for now, so hiding it.
    private var selectedPatient: Patient? = null
    private var patients = mutableSetOf<Patient>()
    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        launchRequest {
            if (appManager.appType.value is AppType.Caregiver) {
                loadPatients()
                selectPatient(appManager.appType.value.patientId)
            } else {
                selectedPatient = null
                patients.clear()
            }
            notifyStateChange(currentState.copy(desc = stepsDesc))
            submitNewPeriodAndRange()
            loadDataForCurrentRangeAndPeriod()
        }
    }

    override fun handleEvent(event: ExerciseEvent) {
        when (event) {
            ExerciseEvent.BackClick -> notifyEffectChanged(ExerciseEffect.GoBack)
            ExerciseEvent.RefreshData -> launchRequest {
                loadDataForCurrentRangeAndPeriod()
            }
            ExerciseEvent.NextClicked -> launchRequest {
                loadNewPeriod(1)
            }
            ExerciseEvent.PrevClicked -> launchRequest {
                loadNewPeriod(-1)
            }
            is ExerciseEvent.RangeChanged -> launchRequest {
                changeRange(event.range)
            }
            is ExerciseEvent.ChangePatient -> launchRequest {
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
        val patientId: Int? = selectedPatient?.userId?.toIntOrNull()
        notifyStateChange(currentState.copy(isLoading = true))
        val entries = runCatching {
            loadExerciseForDataRange(
                startDate = datePeriod.first,
                endDate = datePeriod.second,
                intervalFunction = if (dateRange == HistoryRange.Day) "HOUR" else "DAY",
                patientId = patientId,
            )
        }.onFailure { handleError(it) }.getOrElse {
            List(
                when (dateRange) {
                    HistoryRange.Day -> 1
                    HistoryRange.Week -> 7
                    HistoryRange.Month -> 30
                }
            ) { ExerciseEntry(Date(), 0) }
        }
        sendEntriesToUi(entries)
    }

    private fun sendEntriesToUi(entries: List<ExerciseEntry>) {
        when (dateRange) {
            HistoryRange.Day -> sendDayViewToUi(entries)
            HistoryRange.Week -> sendWeekViewToUi(entries)
            HistoryRange.Month -> sendMonthViewToUi(entries)
        }
    }

    private fun sendDayViewToUi(entries: List<ExerciseEntry>) {
        val sumSteps = "%d min".format(entries.sumOf { it.value / 60 })
        val maxSteps = entries.maxOfOrNull { it.value / 60 } ?: 0
        val yAxiStep = if (maxSteps < 50) 10 else if (maxSteps < 100) 20 else 50
        val customMaxValue = (maxSteps / yAxiStep + 1) * yAxiStep + 10f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillHourGaps()
            .map { ExerciseChartEntry(it.key, it.value.firstOrNull()) }

        val dailyInfo = ActivityGraphScreen.ActivityInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("h a", Locale.US)
                        .format((it as? ExerciseChartEntry)?.date ?: Date())
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
        notifyStateChange(currentState.copy(isLoading = false, information = dailyInfo))
    }

    private fun sendWeekViewToUi(entries: List<ExerciseEntry>) {
        val avgSteps = "%d min".format(entries.map { it.value / 60 }.average().toInt())
        val maxSteps = entries.maxOfOrNull { it.value / 60 } ?: 0
        val yAxiStep = if (maxSteps < 50) 10 else if (maxSteps < 100) 20 else 50
        val customMaxValue = (maxSteps / yAxiStep + 1) * yAxiStep + 10f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map { ExerciseChartEntry(it.key, it.value.firstOrNull()) }

        val weeklyInfo = ActivityGraphScreen.ActivityInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("EE", Locale.US)
                        .format((it as? ExerciseChartEntry)?.date ?: Date())
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
        notifyStateChange(currentState.copy(isLoading = false, information = weeklyInfo))
    }

    private fun sendMonthViewToUi(entries: List<ExerciseEntry>) {
        val avgSteps = "%d min".format(entries.map { it.value / 60 }.average().toInt())
        val maxSteps = entries.maxOfOrNull { it.value / 60 } ?: 0
        val yAxiStep = if (maxSteps < 50) 10 else if (maxSteps < 100) 20 else 50
        val customMaxValue = (maxSteps / yAxiStep + 1) * yAxiStep + 10f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map { ExerciseChartEntry(it.key, it.value.firstOrNull()) }

        val monthlyInfo = ActivityGraphScreen.ActivityInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("d", Locale.US)
                        .format((it as? ExerciseChartEntry)?.date ?: Date())
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
        notifyStateChange(currentState.copy(isLoading = false, information = monthlyInfo))

    }

    private fun getCloudDataForMonth(entry: BarChartEntry): Pair<String, String> {
        val date = (entry as? ExerciseChartEntry)?.date ?: Date()
        return "%d min".format(entry.total.toInt()) to
                SimpleDateFormat("d MMMM", Locale.getDefault()).format(date)
    }

    private fun getCloudDataForWeek(entry: BarChartEntry): Pair<String, String> {
        val date = (entry as? ExerciseChartEntry)?.date ?: Date()
        return "%d min".format(entry.total.toInt()) to
                SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
    }

    private fun getCloudDataForDay(entry: BarChartEntry): Pair<String, String> {
        val date = (entry as? ExerciseChartEntry)?.date ?: Date()
        return "%d min".format(entry.total.toInt()) to
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
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
            notifyEffectChanged(ExerciseEffect.ShowLostCaregiverScreen)
        }
    }

    private fun selectPatient(patientId: String?) {
        val patient = patients.firstOrNull { it.userId == patientId } ?: patients.firstOrNull()
        selectedPatient = patient
        appManager.changePatientId(patient?.userId)
        notifyStateChange(currentState.copy(selectedPatient = selectedPatient, patients = patients.toList()))
    }
}