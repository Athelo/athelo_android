package com.i2asolutions.athelo.presentation.ui.sleepDetails

import com.i2asolutions.athelo.extensions.*
import com.i2asolutions.athelo.presentation.model.chart.BarChartDataSet
import com.i2asolutions.athelo.presentation.model.chart.BarChartEntry
import com.i2asolutions.athelo.presentation.model.sleep.*
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.health.LoadSleepEntriesForDataRangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SleepDetailsViewModel @Inject constructor(
    private val loadSleepEntriesForDataRange: LoadSleepEntriesForDataRangeUseCase,
) : BaseViewModel<SleepDetailEvent, SleepDetailEffect>() {

    private var currentState = SleepDetailViewState(false)
    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private var datePeriod: Pair<Date, Date> = getDayPeriod()
    private var dateRange: HistoryRange = HistoryRange.Day

    private val sleepDesc = "" //todo - not returned from WS for now, so hiding it.

    override fun loadData() {
        notifyStateChanged(currentState.copy(sleepDesc = sleepDesc))
        submitNewPeriodAndRange()
        loadDataForCurrentRangeAndPeriod()
    }

    override fun handleEvent(event: SleepDetailEvent) {
        when (event) {
            SleepDetailEvent.BackClick -> notifyEffectChanged(SleepDetailEffect.GoBack)
            SleepDetailEvent.RefreshData -> {
                loadDataForCurrentRangeAndPeriod()
            }
            SleepDetailEvent.NextClicked -> {
                loadNewPeriod(1)
            }
            SleepDetailEvent.PrevClicked -> {
                loadNewPeriod(-1)
            }
            is SleepDetailEvent.RangeChanged -> {
                changeRange(event.range)
            }
        }
    }

    private fun notifyStateChanged(newState: SleepDetailViewState) {
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
            val entries = loadSleepEntriesForDataRange(datePeriod.first, datePeriod.second)
            sendSleepEntriesToUi(entries)
        }
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
        notifyStateChanged(currentState.copy(isLoading = false, dailyInformation = dailyInfo))
    }

    private fun sendWeekViewToUi(entries: List<SleepEntry>) {
        val avgSleepTime = entries
            .groupBy { it.date }
            .map { it.value.sumOf { it2 -> it2.duration } }
            .average().toInt().displaySecsAsTime()

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
        notifyStateChanged(currentState.copy(isLoading = false, weeklyInformation = weeklyInfo))

    }

    private fun sendMonthViewToUi(entries: List<SleepEntry>) {
        val avgSleepTime = entries
            .groupBy { it.date }
            .map { it.value.sumOf { it2 -> it2.duration } }
            .average().toInt().displaySecsAsTime()
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
        notifyStateChanged(currentState.copy(isLoading = false, monthlyInformation = monthlyInfo))

    }

    private fun getCloudDataForEntry(sleepEntry: BarChartEntry): Pair<String, String> {
        val date = (sleepEntry as? SleepChartEntry)?.date
            ?: (sleepEntry as? SleepMonthChartEntry)?.date
            ?: Date()
        val secs = (sleepEntry.total * 60 * 60).toInt().displaySecsAsTime()
        return secs to SimpleDateFormat("d MMM", Locale.getDefault()).format(date)
    }
}