package com.athelohealth.mobile.presentation.ui.share.home

import androidx.lifecycle.viewModelScope
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.calendar.Day
import com.athelohealth.mobile.presentation.model.calendar.toDay
import com.athelohealth.mobile.presentation.model.health.Symptom
import com.athelohealth.mobile.presentation.model.health.Wellbeing
import com.athelohealth.mobile.presentation.model.home.Feelings
import com.athelohealth.mobile.presentation.model.home.HomeItems
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.connectFitbit.CheckFitbitConnectionStateUseCase
import com.athelohealth.mobile.useCase.connectFitbit.ListenFitbitConnectionStateUseCase
import com.athelohealth.mobile.useCase.health.LoadMySymptomsUseCase
import com.athelohealth.mobile.useCase.health.LoadWellbeingForDayUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import com.athelohealth.mobile.utils.fitbit.FitbitState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appManager: AppManager,
    listenFitbitConnection: ListenFitbitConnectionStateUseCase,
    private val storeUser: StoreUserUseCase,
    private val loadProfile: LoadMyProfileUseCase,
    private val loadWellbeing: LoadWellbeingForDayUseCase,
    private val loadSymptoms: LoadMySymptomsUseCase,
    private val checkFitbitConnection: CheckFitbitConnectionStateUseCase,
) : BaseViewModel<HomeEvent, HomeEffect, HomeViewState>(HomeViewState(isLoading = true, displayName = "", listItems = emptyList())) {
    private lateinit var user: User
    private val symptoms: MutableSet<Symptom> = mutableSetOf()
    private var feeling: Feelings? = null
    private var fitbitState: FitbitState = FitbitState.Unknown

    init {
        notifyStateChange(HomeViewState(isLoading = true, displayName = "", listItems = prepareList()))
    }

    private var currentApp = appManager.appType.value

    init {
        listenFitbitConnection().onEach {
            fitbitState = it
            notifyStateChange(currentState.copy(listItems = prepareList()))
        }.launchIn(viewModelScope)

        appManager.appType.onEach {
            if (currentApp != it) {
                currentApp = it
                notifyStateChange(
                    currentState.copy(
                        listItems = prepareList()
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }
    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
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
            notifyStateChange(
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
            if (appManager.appType.value == AppType.Patient) {
                add(HomeItems.HeaderHome("Let's take care of your health together!"))
                add(feeling?.let {
                    HomeItems.WellbeingHome(it)
                } ?: HomeItems.ButtonHome(
                    R.drawable.ic_laugh,
                    "How are you feeling today?",
                    R.drawable.ic_arrow_gray,
                    "ShowTrackWellbeingScreen"
                ))

                if (feeling != null || symptoms.isNotEmpty()) {
                    add(HomeItems.HeaderHome("Is anything bothering you today? Are you having any new symptoms?"))
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
            }
      //      add(HomeItems.HeaderHome("Have you checked your activity and sleep levels today? This new article might be interesting"))
           add(HomeItems.ButtonHome(R.drawable.ic_person, "How can we make today a better day?",
                   R.drawable.ic_arrow_gray,"ShowTrackWellbeingScreen"))
//            if (fitbitState != FitbitState.Connected) {
//                add(
//                    HomeItems.ButtonHome(
//                        R.drawable.watch,
//                        "Let's connect Fitbit to monitor your sleep!",
//                        R.drawable.ic_arrow_gray,
//                        "ShowConnectScreen"
//                    )
//                )
//            }
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
}