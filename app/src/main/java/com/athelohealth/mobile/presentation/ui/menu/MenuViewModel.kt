package com.athelohealth.mobile.presentation.ui.menu

import androidx.lifecycle.viewModelScope
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.menu.MenuItem
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.health.FitbitAuthorizationUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.ObserveUserChangeUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    appManager: AppManager,
    observerUserChange: ObserveUserChangeUseCase,
    private val loadMyProfile: LoadMyProfileUseCase,
    private val fitbitInit: FitbitAuthorizationUseCase,
) : BaseViewModel<MenuEvent, MenuEffect, MenuViewState>(MenuViewState(
    true,
    emptyList(),
    "",
    null,
    false
)) {

    init {
        observerUserChange().onEach { user ->
            notifyStateChange(
                currentState.copy(
                    displayName = user?.displayName ?: "",
                    image = user?.photo?.image100100
                )
            )
        }.launchIn(viewModelScope)

        appManager.appType.onEach {
            if (currentAppType != it) {
                currentAppType = it
                notifyStateChange(currentState.copy(items = generateItems(it)))
            }
        }.launchIn(viewModelScope)
    }

    override fun pauseLoadingState() {
        notifyStateChange(currentState.copy(isLoading = false))
    }

    private fun generateItems(appType: AppType) =
        if (appType.javaClass == AppType.Caregiver::class.java) generateCaregiverMenu() else generatePatientMenu()

    private var currentAppType: AppType = AppType.Unknown

    override fun loadData() {
        launchRequest {
            val user = loadMyProfile()
            notifyStateChange(
                currentState.copy(
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