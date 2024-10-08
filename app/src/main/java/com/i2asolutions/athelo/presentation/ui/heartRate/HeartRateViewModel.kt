package com.i2asolutions.athelo.presentation.ui.heartRate

import com.i2asolutions.athelo.extensions.*
import com.i2asolutions.athelo.presentation.model.activity.ActivityGraphScreen
import com.i2asolutions.athelo.presentation.model.chart.BarChartDataSet
import com.i2asolutions.athelo.presentation.model.chart.BarChartEntry
import com.i2asolutions.athelo.presentation.model.chart.LineChartDataSet
import com.i2asolutions.athelo.presentation.model.chart.LineChartEntry
import com.i2asolutions.athelo.presentation.model.heartRate.HeartRateBarChartEntry
import com.i2asolutions.athelo.presentation.model.heartRate.HeartRateChartEntry
import com.i2asolutions.athelo.presentation.model.heartRate.HeartRateEntry
import com.i2asolutions.athelo.presentation.model.sleep.HistoryRange
import com.i2asolutions.athelo.presentation.model.sleep.SleepDetailScreen
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.health.LoadHeartRateEntriesForDataRangeUseCase
import com.i2asolutions.athelo.useCase.health.LoadSingleHeartRateEntriesForDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor(
    private val loadHeartRateForDataRange: LoadHeartRateEntriesForDataRangeUseCase,
    private val loadHeartRateForDay: LoadSingleHeartRateEntriesForDayUseCase,
) : BaseViewModel<HeartRateEvent, HeartRateEffect>() {

    private var currentState = HeartRateViewState(false)
    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private var datePeriod: Pair<Date, Date> = getDayPeriod()
    private var dateRange: HistoryRange = HistoryRange.Day

    private val desc = "" //todo - not returned from WS for now, so hiding it.

    override fun loadData() {
        notifyStateChanged(currentState.copy(desc = desc))
        submitNewPeriodAndRange()
        loadDataForCurrentRangeAndPeriod()
    }

    override fun handleEvent(event: HeartRateEvent) {
        when (event) {
            HeartRateEvent.BackClick -> notifyEffectChanged(HeartRateEffect.GoBack)
            HeartRateEvent.RefreshData -> {
                loadDataForCurrentRangeAndPeriod()
            }
            HeartRateEvent.NextClicked -> {
                loadNewPeriod(1)
            }
            HeartRateEvent.PrevClicked -> {
                loadNewPeriod(-1)
            }
            is HeartRateEvent.RangeChanged -> {
                changeRange(event.range)
            }
        }
    }

    private fun notifyStateChanged(newState: HeartRateViewState) {
        currentState = newState
        launchOnUI { _viewState.emit(currentState) }
    }

    private fun loadNewPeriod(diff: Int) {
        if ((diff > 0 && !datePeriod.canMoveForward()) || (diff < 0 && !canMoveBackward())) return
        datePeriod = when (dateRange) {
            HistoryRange.Day -> getDayPeriod(datePeriod, diff)
            HistoryRange.Week -> getWeekPeriod(datePeriod, diff)
            HistoryRange.Month -> getMonthPeriod(datePeriod, diff)
        }
        submitNewPeriodAndRange()
        loadDataForCurrentRangeAndPeriod()
    }

    private fun changeRange(newRange: HistoryRange) {
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

    private fun loadDataForCurrentRangeAndPeriod() {
        launchRequest {
            notifyStateChanged(currentState.copy(isLoading = true))
            val entries = if (dateRange == HistoryRange.Day)
                loadHeartRateForDay(datePeriod.first)
            else
                loadHeartRateForDataRange(
                    datePeriod.first,
                    datePeriod.second,
                    if (dateRange == HistoryRange.Week) "HOUR" else "DAY"
                )
            sendEntriesToUi(entries)
        }
    }

    private fun sendEntriesToUi(entries: List<HeartRateEntry>) {
        when (dateRange) {
            HistoryRange.Day -> sendDayViewToUi(entries)
            HistoryRange.Week -> sendWeekViewToUi(entries)
            HistoryRange.Month -> sendMonthViewToUi(entries)
        }
    }

    private fun sendDayViewToUi(entries: List<HeartRateEntry>) {
        val min = entries.map { it.value }.minOrNull()?.toInt() ?: 0
        val max = entries.map { it.value }.maxOrNull()?.toInt() ?: 0
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
        notifyStateChanged(currentState.copy(isLoading = false, information = dailyInfo))
    }

    private fun sendWeekViewToUi(entries: List<HeartRateEntry>) {
        val min = entries.map { it.value }.minOrNull()?.toInt() ?: 0
        val max = entries.map { it.value }.maxOrNull()?.toInt() ?: 0
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
        notifyStateChanged(currentState.copy(isLoading = false, information = dailyInfo))
    }

    private fun sendMonthViewToUi(entries: List<HeartRateEntry>) {
        val avg = "%d bps".format(entries.map { it.value }.average().toInt())
        val max = entries.map { it.value }.maxOrNull() ?: 0
        val yAxiStep = if (max < 50) 10 else if (max < 100) 20 else 50
        val customMaxValue = (max / yAxiStep + 1) * yAxiStep + 10f

        val chartEntries = entries
            .groupBy { it.date }
            .toSortedMap()
            .fillGaps(datePeriod.first, datePeriod.second)
            .map { HeartRateChartEntry(it.key, it.value.firstOrNull()) }

        val monthlyInfo = ActivityGraphScreen.ActivityLineInformation(
            LineChartDataSet(
                values = chartEntries,
                yAxisFormatter = { it.toInt().toString() },
                xAxisFormatter = {
                    SimpleDateFormat("d", Locale.US)
                        .format((it as? HeartRateChartEntry)?.date ?: Date())
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
        notifyStateChanged(currentState.copy(isLoading = false, monthlyInformation = monthlyInfo))
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
}