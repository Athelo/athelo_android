package com.i2asolutions.athelo.presentation.ui.activity

import com.i2asolutions.athelo.extensions.fillGaps
import com.i2asolutions.athelo.extensions.getLastWeekPeriod
import com.i2asolutions.athelo.presentation.model.activity.ActivityScreen
import com.i2asolutions.athelo.presentation.model.exercise.ExerciseEntry
import com.i2asolutions.athelo.presentation.model.heartRate.HeartRateEntry
import com.i2asolutions.athelo.presentation.model.hrv.HrvEntry
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.model.step.StepEntry
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.health.LoadExerciseForDataRangeUseCase
import com.i2asolutions.athelo.useCase.health.LoadHeartRateEntriesForDataRangeUseCase
import com.i2asolutions.athelo.useCase.health.LoadHrvForDataRangeUseCase
import com.i2asolutions.athelo.useCase.health.LoadStepEntriesForDataRangeUseCase
import com.i2asolutions.athelo.useCase.member.LoadCachedUserUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.utils.AuthorizationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val loadCachedUser: LoadCachedUserUseCase,
    private val loadMyProfile: LoadMyProfileUseCase,
    private val loadStepEntriesForDataRange: LoadStepEntriesForDataRangeUseCase,
    private val loadExerciseForDataRange: LoadExerciseForDataRangeUseCase,
    private val loadHeartRateEntriesForDataRange: LoadHeartRateEntriesForDataRangeUseCase,
    private val loadHrvForDataRange: LoadHrvForDataRangeUseCase,
) : BaseViewModel<ActivityEvent, ActivityEffect>() {

    private var currentState = ActivityViewState(false)
    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private var datePeriod: Pair<Date, Date> = getLastWeekPeriod()

    private val topDesc = ""//todo - not returned from WS for now, so hiding it.

    private lateinit var user: User

    override fun loadData() {
        notifyStateChanged(currentState.copy(topHint = topDesc))
        loadDataForCurrentPeriod()
    }

    override fun handleEvent(event: ActivityEvent) {
        when (event) {
            ActivityEvent.MenuClick -> notifyEffectChanged(ActivityEffect.ShowMenu)
            ActivityEvent.RefreshData -> loadDataForCurrentPeriod()
            ActivityEvent.ConnectSmartWatchClick -> notifyEffectChanged(ActivityEffect.ShowConnectSmartWatchScreen)
            ActivityEvent.MyProfileClick -> notifyEffectChanged(ActivityEffect.ShowMyProfileScreen)
            ActivityEvent.StepsClick -> notifyEffectChanged(ActivityEffect.ShowStepsScreen)
            ActivityEvent.ActivityClick -> notifyEffectChanged(ActivityEffect.ShowActivityScreen)
            ActivityEvent.HeartRateClick -> notifyEffectChanged(ActivityEffect.ShowHeartRateScreen)
            ActivityEvent.HrvClick -> notifyEffectChanged(ActivityEffect.ShowHrvScreen)
        }
    }

    private fun notifyStateChanged(newState: ActivityViewState) {
        currentState = newState
        launchOnUI { _viewState.emit(currentState) }
    }

    private fun loadDataForCurrentPeriod() {
        launchRequest {
            notifyStateChanged(currentState.copy(isLoading = true))

            if (!::user.isInitialized)
                user = loadCachedUser() ?: loadMyProfile() ?: throw AuthorizationException()
            val fitBitConnected = user.fitBitConnected
            notifyStateChanged(
                currentState.copy(
                    isLoading = fitBitConnected,
                    showNotConnected = !fitBitConnected,
                    currentUser = user
                )
            )
            if (!fitBitConnected) return@launchRequest

            val steps = loadSteps()
            val exercise = loadExercises()
            val heartRate = loadHeartRate()
            val hrv = loadHrv()

            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    stepsInformation = steps,
                    activityInformation = exercise,
                    heartRateInformation = heartRate,
                    hrvInformation = hrv
                )
            )
        }
    }

    private suspend fun loadSteps(): ActivityScreen.Steps {
        val stepEntries = runCatching {
            loadStepEntriesForDataRange(
                datePeriod.first,
                datePeriod.second
            )
        }.getOrElse { List(7) { StepEntry(Date(), 0) } }
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

    private suspend fun loadExercises(): ActivityScreen.Activity {
        val stepEntries = runCatching {
            loadExerciseForDataRange(
                datePeriod.first,
                datePeriod.second
            )
        }.getOrElse { List(7) { ExerciseEntry(Date(), 0) } }
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

    private suspend fun loadHeartRate(): ActivityScreen.HeartRate {
        val stepEntries = runCatching {
            loadHeartRateEntriesForDataRange(
                datePeriod.first,
                datePeriod.second
            )
        }.getOrElse { List(7) { HeartRateEntry(Date(), 0) } }
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
        val stepEntries = runCatching {
            loadHrvForDataRange(
                datePeriod.first,
                datePeriod.second
            )
        }.getOrElse { List(7) { HrvEntry(Date(), 0) } }
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