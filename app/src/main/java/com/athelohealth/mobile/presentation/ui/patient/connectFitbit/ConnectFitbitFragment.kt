package com.athelohealth.mobile.presentation.ui.patient.connectFitbit

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.model.home.Tabs
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToHomeWithTab
import com.athelohealth.mobile.utils.routeToMyProfile
import com.athelohealth.mobile.utils.routeToWebView
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