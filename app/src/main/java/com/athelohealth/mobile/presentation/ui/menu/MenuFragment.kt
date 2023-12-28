package com.athelohealth.mobile.presentation.ui.menu

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.model.feedback.FeedbackScreenType
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseComposeFragment<MenuViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        MenuScreen(viewModel = viewModel)
    }

    override val viewModel: MenuViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData()
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                MenuEffect.ShowAskAtheloScreen -> {
                    hideMenu {
                        routeAskQuestion()
                    }
                }
                is MenuEffect.ShowConnectSmartWatchScreen -> {
                    hideMenu {
                        routeToConnectFitbitScreen()
                    }
                }
                MenuEffect.ShowInviteCaregiver -> {
                    hideMenu {
                        routeToFindCaregiver()
                    }
                }
                MenuEffect.ShowMessagesScreen -> {
                    hideMenu {
                        routeToMessages()
                    }
                }
                MenuEffect.ShowMyCaregiversScreen -> {
                    hideMenu {
                        routeToMyCaregivers()
                    }
                }
                MenuEffect.ShowMyProfileScreen -> {
                    hideMenu {
                        routeToMyProfile()
                    }
                }
                MenuEffect.ShowMySymptomsScreen -> {
                    hideMenu {
                        routeToMySymptoms()
                    }
                }
                MenuEffect.ShowSendFeedbackScreen -> {
                    hideMenu {
                        routeToFeedback(FeedbackScreenType.Feedback)
                    }
                }
                MenuEffect.ShowSettingsScreen -> {
                    hideMenu {
                        routeToSettings()
                    }
                }
                MenuEffect.CloseMenuScreen -> hideMenu {}
                MenuEffect.ShowMyWardsScreen -> hideMenu {
                    routeToMyWards()
                }
            }
        }
    }
}