package com.i2asolutions.athelo.presentation.ui.connectFitbit

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.model.home.Tabs
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToHomeWithTab
import com.i2asolutions.athelo.utils.routeToMyProfile
import com.i2asolutions.athelo.utils.routeToWebView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectFitbitFragment : BaseComposeFragment<ConnectFitbitViewModel>() {
    override val viewModel: ConnectFitbitViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        ConnectFitbitScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                ConnectFitbitEffect.ShowActivityPageScreen -> routeToHomeWithTab(Tabs.Activity)
                ConnectFitbitEffect.ShowHomePageScreen -> routeToHomeWithTab(Tabs.Home)
                is ConnectFitbitEffect.ShowConnectFitbitScreen -> routeToWebView(effect.url)
                ConnectFitbitEffect.ShowPrevScreen -> routeToBackScreen()
                ConnectFitbitEffect.ShowMenuScreen -> openMenu()
                ConnectFitbitEffect.ShowMyProfileScreen -> routeToMyProfile()
            }
        }
    }

}