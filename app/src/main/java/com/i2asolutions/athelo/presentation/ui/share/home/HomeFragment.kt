package com.i2asolutions.athelo.presentation.ui.share.home

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.*
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