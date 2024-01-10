package com.athelohealth.mobile.presentation.ui.share.heartRate

import com.athelohealth.mobile.extensions.*
import com.athelohealth.mobile.presentation.model.activity.ActivityGraphScreen
import com.athelohealth.mobile.presentation.model.chart.BarChartDataSet
import com.athelohealth.mobile.presentation.model.chart.BarChartEntry
import com.athelohealth.mobile.presentation.model.chart.LineChartDataSet
import com.athelohealth.mobile.presentation.model.chart.LineChartEntry
import com.athelohealth.mobile.presentation.model.heartRate.EmptyHeartRateChartEntry
import com.athelohealth.mobile.presentation.model.heartRate.HeartRateBarChartEntry
import com.athelohealth.mobile.presentation.model.heartRate.HeartRateChartEntry
import com.athelohealth.mobile.presentation.model.heartRate.HeartRateEntry
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.HistoryRange
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadHeartRateEntriesForDataRangeUseCase
import com.athelohealth.mobile.useCase.health.LoadSingleHeartRateEntriesForDayUseCase
import com.athelohealth.mobile.useCase.patients.LoadPatientsUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.athelohealth.mobile.utils.app.patientId
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val loadHeartRateForDataRange: LoadHeartRateEntriesForDataRangeUseCase,
    private val loadHeartRateForDay: LoadSingleHeartRateEntriesForDayUseCase,
) : BaseViewModel<HeartRateEvent, HeartRateEffect, HeartRateViewState>(HeartRateViewState(false)) {
    private var datePeriod: Pair<Date, Date> = getDayPeriod()
    private var dateRange: HistoryRange = HistoryRange.Day

    private val desc = "" //todo - not returned from WS for now, so hiding it.
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
            notifyStateChange(currentState.copy(desc = desc))
            submitNewPeriodAndRange()
            loadDataForCurrentRangeAndPeriod()
        }
    }

    override fun handleEvent(event: HeartRateEvent) {
        when (event) {
            HeartRateEvent.BackClick -> notifyEffectChanged(HeartRateEffect.GoBack)
            HeartRateEvent.RefreshData -> launchRequest {
                loadDataForCurrentRangeAndPeriod()
            }
            HeartRateEvent.NextClicked -> launchRequest {
                loadNewPeriod(1)
            }
            HeartRateEvent.PrevClicked -> launchRequest {
                loadNewPeriod(-1)
            }
            is HeartRateEvent.RangeChanged -> launchRequest {
                changeRange(event.range)
            }
            is HeartRateEvent.ChangePatient -> launchRequest {
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
        val patientId: Int? = selectedPatient?.userId?.toIntOrNull()
        val entries = if (dateRange == HistoryRange.Day)
            runCatching {
                loadHeartRateForDay(
                    datePeriod.first,
                    patientId
                )
            }.onFailure { handleError(it) }.getOrElse {
                List(1) {
                    HeartRateEntry(
                        Date(), 0
                    )
                }
            }
        else
            runCatching {
                loadHeartRateForDataRange(
                    datePeriod.first,
                    datePeriod.second,
                    if (dateRange == HistoryRange.Week) "HOUR" else "DAY",
                    patientId = patientId,
                )
            }.onFailure { handleError(it) }.getOrElse {
                List(if (dateRange == HistoryRange.Week) 7 else 30) {
                    HeartRateEntry(
                        Date(),
                        0
                    )
                }
            }
        sendEntriesToUi(entries)
    }

    private fun sendEntriesToUi(entries: List<HeartRateEntry>) {
        when (dateRange) {
            HistoryRange.Day -> sendDayViewToUi(entries)
            HistoryRange.Week -> sendWeekViewToUi(entries)
            HistoryRange.Month -> sendMonthViewToUi(entries)
        }
    }

    private fun sendDayViewToUi(entries: List<HeartRateEntry>) {
        val min = entries.minOfOrNull { it.value }?.toInt() ?: 0
        val max = entries.maxOfOrNull { it.value }?.toInt() ?: 0
        val avg = if (min == max && min == 0) "0 bps" else "%d - %d bps".format(min, max)

        val yAxiStep = if (max < 50) 10 else if (max < 100) 20 else 50
        val customMaxValue = (max / yAxiStep + 1) * yAxiStep + 10f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillHourGaps()
            .map { HeartRateBarChartEntry(it.value, it.key) }

        val dailyInfo = ActivityGraphScreen.ActivityInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("h a", Locale.US)
                        .format((it as? HeartRateBarChartEntry)?.date ?: Date())
                },
                cloudFormatter = ::getCloudDataForDay,
                xLabelStep = 4,
                xAxisMinLabel = 0,
                xAxisMaxLabel = 23,
                yLabelStep = yAxiStep,
                customMaxValue = customMaxValue,
                drawYLines = false,
                hourMode = true,
            ),
            avg
        )
        notifyStateChange(currentState.copy(isLoading = false, information = dailyInfo))
    }

    private fun sendWeekViewToUi(entries: List<HeartRateEntry>) {
        val min = entries.minOfOrNull { it.value }?.toInt() ?: 0
        val max = entries.maxOfOrNull { it.value }?.toInt() ?: 0
        val avg = if (min == max && min == 0) "0 bps" else "%d - %d bps".format(min, max)
        val yAxiStep = if (max < 50) 10 else if (max < 100) 20 else 50
        val customMaxValue = (max / yAxiStep + 1) * yAxiStep + 10f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map { HeartRateBarChartEntry(it.value, it.key) }

        val dailyInfo = ActivityGraphScreen.ActivityInformation(
            BarChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("EE", Locale.US)
                        .format((it as? HeartRateBarChartEntry)?.date ?: Date())
                },
                cloudFormatter = ::getCloudDataForWeek,
                yLabelStep = yAxiStep,
                customMaxValue = customMaxValue,
                drawYLines = true,
                barRatio = 0.3f,
            ),
            avg
        )
        notifyStateChange(currentState.copy(isLoading = false, information = dailyInfo))
    }

    private fun sendMonthViewToUi(entries: List<HeartRateEntry>) {
        val avg = "%d bps".format(entries.map { it.value }.average().toInt())
        val max = entries.maxOfOrNull { it.value } ?: 0
        val yAxiStep = if (max < 50) 10 else if (max < 100) 20 else 50
        val customMaxValue = (max / yAxiStep + 1) * yAxiStep + 10f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map {
                if (it.value.isEmpty()) EmptyHeartRateChartEntry(it.key) else HeartRateChartEntry(
                    it.key,
                    it.value.firstOrNull()
                )
            }

        val monthlyInfo = ActivityGraphScreen.ActivityLineInformation(
            LineChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("d", Locale.US)
                        .format(
                            (it as? HeartRateChartEntry)?.date
                                ?: (it as? EmptyHeartRateChartEntry)?.date ?: Date()
                        )
                },
                cloudFormatter = ::getCloudDataForMonth,
                xLabelStep = 5,
                xAxisMinLabel = 0,
                xAxisMaxLabel = 30,
                xAxisMaxValue = 31,
                lineColor = 0xFF68951B.toInt(),
                backgroundGradient = listOf(0x5468951B, 0x0068951B),
                yLabelStep = yAxiStep,
                customMaxValue = customMaxValue,
                drawYLines = false,
                hourMode = false,
            ),
            avg
        )
        notifyStateChange(currentState.copy(isLoading = false, monthlyInformation = monthlyInfo))
    }

    private fun getCloudDataForDay(entry: BarChartEntry): Pair<String, String> {
        val date = (entry as? HeartRateBarChartEntry)?.date ?: Date()
        return "%d - %d bps".format(
            entry.values.minOfOrNull { it.value.toInt() } ?: 0,
            entry.values.maxOfOrNull { it.value.toInt() } ?: 0) to
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
    }

    private fun getCloudDataForWeek(entry: BarChartEntry): Pair<String, String> {
        val date = (entry as? HeartRateBarChartEntry)?.date ?: Date()
        return "%d - %d bps".format(
            entry.values.minOfOrNull { it.value.toInt() } ?: 0,
            entry.values.maxOfOrNull { it.value.toInt() } ?: 0) to
                SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
    }

    private fun getCloudDataForMonth(entry: LineChartEntry): Pair<String, String> {
        val date = (entry as? HeartRateChartEntry)?.date ?: Date()
        return "%d bps".format(entry.value.toInt()) to
                SimpleDateFormat("d MMMM", Locale.getDefault()).format(date)
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
            notifyEffectChanged(HeartRateEffect.ShowLostCaregiverScreen)
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