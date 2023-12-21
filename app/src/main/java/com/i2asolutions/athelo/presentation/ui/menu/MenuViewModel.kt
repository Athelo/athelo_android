package com.i2asolutions.athelo.presentation.ui.menu

import androidx.lifecycle.viewModelScope
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.caregiver.Caregiver
import com.i2asolutions.athelo.presentation.model.menu.MenuItem
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.health.FitbitAuthorizationUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.useCase.member.ObserveUserChangeUseCase
import com.i2asolutions.athelo.utils.app.AppManager
import com.i2asolutions.athelo.utils.app.AppType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    appManager: AppManager,
    observerUserChange: ObserveUserChangeUseCase,
    private val loadMyProfile: LoadMyProfileUseCase,
    private val fitbitInit: FitbitAuthorizationUseCase,
) :
    BaseViewModel<MenuEvent, MenuEffect>() {
    private var currentStatus = MenuViewState(
        true,
        generateItems(appManager.appType.value),
        "",
        null,
        false
    )

    private fun generateItems(appType: AppType) =
        if (appType.javaClass == AppType.Caregiver::class.java) generateCaregiverMenu() else generatePatientMenu()

    private val _viewState = MutableStateFlow(currentStatus)
    val viewState = _viewState.asStateFlow()
    private var currentAppType: AppType = AppType.Unknown

    init {
        observerUserChange().onEach { user ->
            notifyStateChange(
                currentStatus.copy(
                    displayName = user?.displayName ?: "",
                    image = user?.photo?.image100100
                )
            )
        }.launchIn(viewModelScope)

        appManager.appType.onEach {
            if (currentAppType != it) {
                currentAppType = it
                notifyStateChange(currentStatus.copy(items = generateItems(it)))
            }
        }.launchIn(viewModelScope)
    }

    override fun loadData() {
        launchRequest {
            val user = loadMyProfile()
            notifyStateChange(
                currentStatus.copy(
                    displayName = user?.displayName ?: "",
                    image = user?.photo?.image100100
                )
            )
        }
    }

    override fun handleEvent(event: MenuEvent) {
        when (event) {
            MenuEvent.CloseClick -> notifyEffectChanged(MenuEffect.CloseMenuScreen)
            is MenuEvent.ItemClick -> selfBlockRun {
                launchRequest {
                    notifyEffectChanged(
                        when (event.deeplink) {
                            deepLinkMyProfile -> MenuEffect.ShowMyProfileScreen
                            deepLinkMessages -> MenuEffect.ShowMessagesScreen
                            deepLinkMySymptoms -> MenuEffect.ShowMySymptomsScreen
                            deepLinkMyCaregivers -> MenuEffect.ShowMyCaregiversScreen
                            deepLinkInviteCaregiver -> MenuEffect.ShowInviteCaregiver
                            deepLinkSettings -> MenuEffect.ShowSettingsScreen
                            deepLinkConnectWatch -> MenuEffect.ShowConnectSmartWatchScreen(
                                fitbitInit()
                            )
                            deepLinkAskAthelo -> MenuEffect.ShowAskAtheloScreen
                            deepLinkSendFeedback -> MenuEffect.ShowSendFeedbackScreen
                            deepLinkMyWards -> MenuEffect.ShowMyWardsScreen
                            else -> MenuEffect.CloseMenuScreen
                        }
                    )
                }
            }
            MenuEvent.UserClick -> selfBlockRun { notifyEffectChanged(MenuEffect.CloseMenuScreen) }
        }
    }

    private fun notifyStateChange(newState: MenuViewState) {
        currentStatus = newState
        launchOnUI {
            _viewState.emit(currentStatus)
        }
    }

    private fun generatePatientMenu() = arrayListOf(
        MenuItem("My profile", R.drawable.ic_menu_my_profil, deeplink = deepLinkMyProfile),
        MenuItem("Messages", R.drawable.ic_menu_messages, deeplink = deepLinkMessages),
        MenuItem("My symptoms", R.drawable.ic_menu_symptoms, deepLinkMySymptoms),
        MenuItem("My caregivers", R.drawable.ic_menu_my_caregiver, deepLinkMyCaregivers),
        MenuItem(
            "Invite a caregiver",
            R.drawable.ic_menu_inv_caregiver,
            deepLinkInviteCaregiver
        ),
        MenuItem("Settings", R.drawable.ic_menu_settings, deepLinkSettings),
//        MenuItem("Connect smart watch", R.drawable.ic_menu_watch, deepLinkConnectWatch),
//            MenuItem("Ask Athelo", R.drawable.ic_menu_ask_athelo, deepLinkAskAthelo),
        MenuItem("Ask Athelo", R.drawable.ic_menu_ask_athelo, deepLinkSendFeedback),
    )

    private fun generateCaregiverMenu() = arrayListOf(
        MenuItem("My profile", R.drawable.ic_menu_my_profil, deeplink = deepLinkMyProfile),
        MenuItem("My wards", R.drawable.ic_menu_symptoms, deepLinkMyWards),
        MenuItem("Messages", R.drawable.ic_menu_messages, deeplink = deepLinkMessages),
//        MenuItem("My symptoms", R.drawable.ic_menu_symptoms, deepLinkMySymptoms),
        MenuItem("Settings", R.drawable.ic_menu_settings, deepLinkSettings),
//        MenuItem("Connect smart watch", R.drawable.ic_menu_watch, deepLinkConnectWatch),
//            MenuItem("Ask Athelo", R.drawable.ic_menu_ask_athelo, deepLinkAskAthelo),
        MenuItem("Ask Athelo", R.drawable.ic_menu_ask_athelo, deepLinkSendFeedback),
    )

    companion object {
        const val deepLinkMyProfile = "MyProfile"
        const val deepLinkMessages = "Messages"
        const val deepLinkMySymptoms = "MySymptoms"
        const val deepLinkMyCaregivers = "MyCaregivers"
        const val deepLinkInviteCaregiver = "InviteCaregiver"
        const val deepLinkSettings = "Settings"
        const val deepLinkConnectWatch = "ConnectSmartWatch"
        const val deepLinkAskAthelo = "AskAthelo"
        const val deepLinkSendFeedback = "SendFeedback"
        const val deepLinkMyWards = "MyWards"
    }
}