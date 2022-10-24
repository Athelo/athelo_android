package com.i2asolutions.athelo.presentation.ui.authorization.additionalInformation

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToConnectFitbitScreen
import com.i2asolutions.athelo.utils.routeToHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdditionalInfoFragment : BaseComposeFragment<AdditionalInfoViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        ShowAdditionalInfoScreen(viewModel)
    }
    override val viewModel: AdditionalInfoViewModel by viewModels()
    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                AdditionalInfoEffect.ShowMainScreen -> routeToHome()
                AdditionalInfoEffect.ShowConnectSmartWatchScreen -> routeToConnectFitbitScreen(true)
            }
        }
    }

}