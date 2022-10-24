package com.i2asolutions.athelo.presentation.ui.home

import androidx.lifecycle.viewModelScope
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.calendar.toDay
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.model.health.Wellbeing
import com.i2asolutions.athelo.presentation.model.home.Feelings
import com.i2asolutions.athelo.presentation.model.home.HomeItems
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.connectFitbit.CheckFitbitConnectionStateUseCase
import com.i2asolutions.athelo.useCase.connectFitbit.ListenFitbitConnectionStateUseCase
import com.i2asolutions.athelo.useCase.health.LoadMySymptomsUseCase
import com.i2asolutions.athelo.useCase.health.LoadWellbeingForDayUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.useCase.member.StoreUserUseCase
import com.i2asolutions.athelo.utils.AuthorizationException
import com.i2asolutions.athelo.utils.fitbit.FitbitState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    listenFitbitConnection: ListenFitbitConnectionStateUseCase,
    private val storeUser: StoreUserUseCase,
    private val loadProfile: LoadMyProfileUseCase,
    private val loadWellbeing: LoadWellbeingForDayUseCase,
    private val loadSymptoms: LoadMySymptomsUseCase,
    private val checkFitbitConnection: CheckFitbitConnectionStateUseCase,
) :
    BaseViewModel<HomeEvent, HomeEffect>() {
    private lateinit var user: User
    private val symptoms: MutableSet<Symptom> = mutableSetOf()
    private var feeling: Feelings? = null
    private var fitbitState: FitbitState = FitbitState.Unknown

    private var currentState =
        HomeViewState(isLoading = true, displayName = "", listItems = prepareList())

    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    init {
        listenFitbitConnection().onEach {
            fitbitState = it
            notifyStateChanged(currentState.copy(listItems = prepareList()))
        }.launchIn(viewModelScope)
    }

    override fun loadData() {
        notifyStateChanged(currentState.copy(isLoading = true))
        launchRequest {
            checkFitbitConnection()
            val userTmp = loadProfile().also { storeUser(user = it) }
                ?: throw AuthorizationException("Looks like your user has been logout. Please Log in to use this app")
            if (!::user.isInitialized || userTmp != user) {
                user = userTmp
            }
            symptoms.clear()
            val day = Calendar.getInstance().toDay()
            loadAllSymptoms(day)
            feeling = loadAllWellbeingAndReturnLatest(day)?.toFeelings()
            notifyStateChanged(
                currentState.copy(
                    isLoading = false,
                    displayName = user.displayName ?: "",
                    userAvatar = user.photo?.image5050,
                    listItems = prepareList()
                )
            )
        }
    }

    private suspend fun loadAllSymptoms(day: Day) {
        var firstRun = true
        var nextUrl: String? = null
        while (firstRun || nextUrl != null) {
            firstRun = false
            val response = loadSymptoms(nextUrl, day)
            nextUrl = response.next
            if (response.result.isNotEmpty())
                response.result.forEach { symptom ->
                    if (symptoms.all { it.name != symptom.name }) {
                        symptoms.add(symptom)
                    }
                }
        }
    }

    private suspend fun loadAllWellbeingAndReturnLatest(day: Day): Wellbeing? {
        var firstRun = true
        var nextUrl: String? = null
        val result = mutableSetOf<Wellbeing>()
        while (firstRun || nextUrl != null) {
            firstRun = false
            val response = loadWellbeing(nextUrl, day)
            nextUrl = response.next
            if (response.result.isNotEmpty())
                result.addAll(response.result)
        }
        return result.asSequence().sortedByDescending { it.id }.firstOrNull()
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.MenuClick -> notifyEffectChanged(HomeEffect.ShowMenuScreen)
            is HomeEvent.ItemClick -> handleItem(event.item)
            HomeEvent.MyProfileClick -> notifyEffectChanged(HomeEffect.ShowMyProfileScreen)
        }
    }

    private fun handleItem(item: HomeItems) {
        when (item) {
            is HomeItems.ButtonHome -> {
                when (item.deeplink) {
                    "ShowTrackWellbeingScreen" -> notifyEffectChanged(HomeEffect.ShowTrackWellbeingScreen)
                    "ShowNewsScreen" -> notifyEffectChanged(HomeEffect.ShowNewsScreen)
                    "ShowConnectScreen" -> notifyEffectChanged(HomeEffect.ShowConnectScreen)
                    "ShowChatScreen" -> notifyEffectChanged(HomeEffect.ShowChatScreen)
                }
            }
            is HomeItems.HeaderHome -> {}
            is HomeItems.SymptomsHome -> {}
            is HomeItems.WellbeingHome -> {}
        }
    }

    private fun prepareList(): List<HomeItems> {
        return buildList {
            add(HomeItems.HeaderHome("Let's take care of your mental health together!"))
            add(feeling?.let {
                HomeItems.WellbeingHome(it)
            } ?: HomeItems.ButtonHome(
                R.drawable.ic_laugh,
                "How are you Feeling today?\nIt is important",
                R.drawable.ic_arrow_gray,
                "ShowTrackWellbeingScreen"
            ))

            if (feeling != null || symptoms.isNotEmpty()) {
                add(HomeItems.HeaderHome("What are your symptoms today? Let's control them together!"))
                if (symptoms.isNotEmpty()) {
                    add(HomeItems.SymptomsHome(symptoms.toList()))
                }
                add(
                    HomeItems.ButtonHome(
                        R.drawable.symptoms,
                        "Update feelings and symptoms",
                        R.drawable.ic_arrow_gray,
                        "ShowTrackWellbeingScreen"
                    )
                )
            }
            add(HomeItems.HeaderHome("We're glad to see you! Let's get to know the app Athelo!"))
//            add(HomeItems.ButtonHome(R.drawable.ic_person, "We recommend a brisk\nwalk today."))
            if (fitbitState != FitbitState.Connected) {
                add(
                    HomeItems.ButtonHome(
                        R.drawable.watch,
                        "Let's connect Fitbit to monitor your sleep!",
                        R.drawable.ic_arrow_gray,
                        "ShowConnectScreen"
                    )
                )
            }
            add(
                HomeItems.ButtonHome(
                    R.drawable.news_on,
                    "Let's find interesting articles for you",
                    R.drawable.ic_arrow_gray,
                    "ShowNewsScreen"
                )
            )
            add(
                HomeItems.ButtonHome(
                    R.drawable.ic_community,
                    "Let's find you a community to chat ",
                    R.drawable.ic_arrow_gray,
                    "ShowChatScreen"
                )
            )
        }
    }

    fun notifyStateChanged(newState: HomeViewState) {
        currentState = newState
        launchOnUI {
            _viewState.emit(currentState)
        }
    }
}