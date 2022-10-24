package com.i2asolutions.athelo.presentation.ui.sleepSummary

import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.displaySecsAsTime
import com.i2asolutions.athelo.extensions.getWeekPeriod
import com.i2asolutions.athelo.presentation.model.chart.CircleChartDataSet
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.model.sleep.SleepLevel
import com.i2asolutions.athelo.presentation.model.sleep.SleepSummaryScreen.*
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.presentation.ui.theme.lightOlivaceous
import com.i2asolutions.athelo.useCase.deviceConfig.LoadDeviceConfigUseCase
import com.i2asolutions.athelo.useCase.health.LoadSleepEntriesForDataRangeUseCase
import com.i2asolutions.athelo.useCase.health.LoadSleepEntriesForTodayUseCase
import com.i2asolutions.athelo.useCase.member.LoadCachedUserUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.utils.AuthorizationException
import com.i2asolutions.athelo.utils.conectivity.NetWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SleepSummaryViewModel @Inject constructor(
    private val loadCachedUser: LoadCachedUserUseCase,
    private val loadMyProfile: LoadMyProfileUseCase,
    private val loadDeviceConfig: LoadDeviceConfigUseCase,
    private val loadSleepEntriesForYesterday: LoadSleepEntriesForTodayUseCase,
    private val loadSleepEntriesForDataRange: LoadSleepEntriesForDataRangeUseCase,
) : BaseViewModel<SleepSummaryEvent, SleepEffect>() {

    private var currentState = SleepViewState(false)
    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private lateinit var user: User

    override fun loadData() {
        if (NetWorkManager.isDisconnected) errorNoInternet()
        else {
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

        }
    }

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    private fun loadSleepSettings() {
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
                loadSleepEntriesForDataRange(lastWeekRange.first, lastWeekRange.second)
            val results = loadSleepEntriesForYesterday()
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

            notifyStateChanged(
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

    private fun notifyStateChanged(newState: SleepViewState) {
        currentState = newState
        launchOnUI { _viewState.emit(currentState) }
    }
}