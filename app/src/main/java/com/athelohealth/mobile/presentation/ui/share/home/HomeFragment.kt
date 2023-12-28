package com.athelohealth.mobile.presentation.ui.share.home

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseComposeFragment<HomeViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        HomeScreen(viewModel = viewModel)
    }
    override val viewModel: HomeViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                HomeEffect.ShowMenuScreen -> openMenu()
                HomeEffect.ShowMyProfileScreen -> routeToMyProfile()
                HomeEffect.ShowTrackWellbeingScreen -> routeToTrackWellbeing()
                HomeEffect.ShowChatScreen -> routeToChatList()
                HomeEffect.ShowConnectScreen -> routeToConnectFitbitScreen()
                HomeEffect.ShowNewsScreen -> routeToNews()
            }
        }
    }
}