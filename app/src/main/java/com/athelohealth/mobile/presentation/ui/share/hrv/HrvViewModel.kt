package com.athelohealth.mobile.presentation.ui.share.hrv

import com.athelohealth.mobile.extensions.*
import com.athelohealth.mobile.presentation.model.activity.ActivityGraphScreen
import com.athelohealth.mobile.presentation.model.chart.LineChartDataSet
import com.athelohealth.mobile.presentation.model.chart.LineChartEntry
import com.athelohealth.mobile.presentation.model.hrv.EmptyHrvChartEntry
import com.athelohealth.mobile.presentation.model.hrv.HrvChartEntry
import com.athelohealth.mobile.presentation.model.hrv.HrvEntry
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.HistoryRange
import com.athelohealth.mobile.presentation.model.sleep.SleepDetailScreen
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.LoadHrvForDataRangeUseCase
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
class HrvViewModel @Inject constructor(
    private val appManager: AppManager,
    private val loadPatients: LoadPatientsUseCase,
    private val loadHrvForDataRange: LoadHrvForDataRangeUseCase
) : BaseViewModel<HrvEvent, HrvEffect>() {

    private var currentState = HrvViewState(false)
    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private var datePeriod: Pair<Date, Date> = getDayPeriod()
    private var dateRange: HistoryRange = HistoryRange.Day

    private val desc = "" //todo - not returned from WS for now, so hiding it.
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
            notifyStateChanged(currentState.copy(desc = desc))
            submitNewPeriodAndRange()
            loadDataForCurrentRangeAndPeriod()
        }
    }

    override fun handleEvent(event: HrvEvent) {
        when (event) {
            HrvEvent.BackClick -> notifyEffectChanged(HrvEffect.GoBack)
            HrvEvent.RefreshData -> launchRequest {
                loadDataForCurrentRangeAndPeriod()
            }
            HrvEvent.NextClicked -> launchRequest {
                loadNewPeriod(1)
            }
            HrvEvent.PrevClicked -> launchRequest {
                loadNewPeriod(-1)
            }
            is HrvEvent.RangeChanged -> launchRequest {
                changeRange(event.range)
            }
            is HrvEvent.ChangePatient -> launchRequest {
                selectPatient(event.patient.userId)
                submitNewPeriodAndRange()
                loadDataForCurrentRangeAndPeriod()
            }
        }
    }

    private fun notifyStateChanged(newState: HrvViewState) {
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
        val entries = runCatching {
            loadHrvForDataRange(
                startDate = datePeriod.first,
                endDate = datePeriod.second,
                intervalFunction = if (dateRange == HistoryRange.Day) "HOUR" else "DAY",
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
                HrvEntry(Date(), 0)
            }
        }
        sendEntriesToUi(entries)
    }

    private fun sendEntriesToUi(entries: List<HrvEntry>) {
        when (dateRange) {
            HistoryRange.Day -> sendDayViewToUi(entries)
            HistoryRange.Week -> sendWeekViewToUi(entries)
            HistoryRange.Month -> sendMonthViewToUi(entries)
        }
    }

    private fun sendDayViewToUi(entries: List<HrvEntry>) {
        val avg = "%d ms".format(entries.map { it.value }.average().toInt())
        val max = entries.map { it.value }.maxOrNull() ?: 0
        val yAxiStep = if (max < 300) 50 else if (max < 500) 100 else 250
        val customMaxValue = (max / yAxiStep + 1) * yAxiStep + 100f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillHourGaps()
            .map {
                if (it.value.isEmpty()) EmptyHrvChartEntry(it.key) else HrvChartEntry(
                    it.key,
                    it.value.firstOrNull()
                )
            }

        val dailyInfo = ActivityGraphScreen.ActivityLineInformation(
            LineChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("h a", Locale.US)
                        .format((it as? HrvChartEntry)?.date ?: Date())
                },
                cloudFormatter = ::getCloudDataForDay,
                xLabelStep = 4,
                xAxisMinLabel = 0,
                xAxisMaxLabel = 23,
                xAxisMaxValue = 23,
                lineColor = 0xFF68951B.toInt(),
                backgroundGradient = listOf(0x5468951B, 0x0068951B),
                yLabelStep = yAxiStep,
                customMaxValue = customMaxValue,
                drawYLines = false,
                hourMode = true,
            ),
            avg
        )
        notifyStateChanged(currentState.copy(isLoading = false, information = dailyInfo))
    }

    private fun sendWeekViewToUi(entries: List<HrvEntry>) {
        val avg = "%d ms".format(entries.map { it.value }.average().toInt())
        val max = entries.map { it.value }.maxOrNull() ?: 0
        val yAxiStep = if (max < 300) 50 else if (max < 500) 100 else 250
        val customMaxValue = (max / yAxiStep + 1) * yAxiStep + 50f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map {
                if (it.value.isEmpty()) EmptyHrvChartEntry(it.key)
                else HrvChartEntry(it.key, it.value.firstOrNull())
            }

        val weeklyInfo = ActivityGraphScreen.ActivityLineInformation(
            LineChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("EE", Locale.US)
                        .format((it as? HrvChartEntry)?.date ?: Date())
                },
                cloudFormatter = ::getCloudDataForWeek,
                xLabelStep = 1,
                xAxisMinLabel = 0,
                xAxisMaxLabel = 6,
                xAxisMaxValue = 6,
                lineColor = 0xFF68951B.toInt(),
                backgroundGradient = listOf(0x5468951B, 0x0068951B),
                yLabelStep = yAxiStep,
                customMaxValue = customMaxValue,
                drawYLines = false,
                hourMode = false,
            ),
            avg
        )
        notifyStateChanged(currentState.copy(isLoading = false, information = weeklyInfo))
    }

    private fun sendMonthViewToUi(entries: List<HrvEntry>) {
        val avg = "%d ms".format(entries.map { it.value }.average().toInt())
        val max = entries.map { it.value }.maxOrNull() ?: 0
        val yAxiStep = if (max < 300) 50 else if (max < 500) 100 else 250
        val customMaxValue = (max / yAxiStep + 1) * yAxiStep + 50f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map {
                if (it.value.isEmpty()) EmptyHrvChartEntry(it.key) else HrvChartEntry(
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
                        .format((it as? HrvChartEntry)?.date ?: Date())
                },
                cloudFormatter = ::getCloudDataForWeekMonth,
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
        notifyStateChanged(currentState.copy(isLoading = false, information = monthlyInfo))
    }

    private fun getCloudDataForDay(entry: LineChartEntry): Pair<String, String> {
        val date = (entry as? HrvChartEntry)?.date ?: Date()
        return "%d ms".format(entry.value.toInt()) to
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(date)
    }

    private fun getCloudDataForWeek(entry: LineChartEntry): Pair<String, String> {
        val date = (entry as? HrvChartEntry)?.date ?: Date()
        return "%d ms".format(entry.value.toInt()) to
                SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
    }

    private fun getCloudDataForWeekMonth(entry: LineChartEntry): Pair<String, String> {
        val date = (entry as? HrvChartEntry)?.date ?: Date()
        return "%d ms".format(entry.value.toInt()) to
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
            notifyEffectChanged(HrvEffect.ShowSelectRoleScreen)
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