package com.i2asolutions.athelo.presentation.ui.share.heartRate

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToLostCaregiverAccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeartRateFragment : BaseComposeFragment<HeartRateViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        HeartRateScreen(viewModel = viewModel)
    }
    override val viewModel: HeartRateViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                HeartRateEffect.GoBack -> routeToBackScreen()
                HeartRateEffect.ShowLostCaregiverScreen -> routeToLostCaregiverAccess()
            }
        }
    }
}