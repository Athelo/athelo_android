package com.i2asolutions.athelo.presentation.ui.steps

import com.i2asolutions.athelo.extensions.*
import com.i2asolutions.athelo.presentation.model.activity.ActivityGraphScreen
import com.i2asolutions.athelo.presentation.model.chart.BarChartDataSet
import com.i2asolutions.athelo.presentation.model.chart.BarChartEntry
import com.i2asolutions.athelo.presentation.model.sleep.HistoryRange
import com.i2asolutions.athelo.presentation.model.sleep.SleepDetailScreen
import com.i2asolutions.athelo.presentation.model.step.StepChartEntry
import com.i2asolutions.athelo.presentation.model.step.StepEntry
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.health.LoadStepEntriesForDataRangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class StepsViewModel @Inject constructor(
    private val loadStepEntriesForDataRange: LoadStepEntriesForDataRangeUseCase
) : BaseViewModel<StepsEvent, StepsEffect>() {

    private var currentState = StepsViewState(false)
    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private var datePeriod: Pair<Date, Date> = getDayPeriod()
    private var dateRange: HistoryRange = HistoryRange.Day

    private val stepsDesc = "" //todo - not returned from WS for now, so hiding it.

    override fun loadData() {
        notifyStateChanged(currentState.copy(desc = stepsDesc))
        submitNewPeriodAndRange()
        loadDataForCurrentRangeAndPeriod()
    }

    override fun handleEvent(event: StepsEvent) {
        when (event) {
            StepsEvent.BackClick -> notifyEffectChanged(StepsEffect.GoBack)
            StepsEvent.RefreshData -> {
                loadDataForCurrentRangeAndPeriod()
            }
            StepsEvent.NextClicked -> {
                loadNewPeriod(1)
            }
            StepsEvent.PrevClicked -> {
                loadNewPeriod(-1)
            }
            is StepsEvent.RangeChanged -> {
                changeRange(event.range)
            }
        }
    }

    private fun notifyStateChanged(newState: StepsViewState) {
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
            val entries = loadStepEntriesForDataRange(
                datePeriod.first,
                datePeriod.second,
                if (dateRange == HistoryRange.Day) "HOUR" else "DAY"
            )
            sendEntriesToUi(entries)
        }
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
}