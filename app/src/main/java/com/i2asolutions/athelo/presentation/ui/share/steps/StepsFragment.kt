package com.i2asolutions.athelo.presentation.ui.share.steps

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToLostCaregiverAccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepsFragment : BaseComposeFragment<StepsViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        StepsScreen(viewModel = viewModel)
    }
    override val viewModel: StepsViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                StepsEffect.GoBack -> routeToBackScreen()
                StepsEffect.ShowLostCaregiverScreen -> routeToLostCaregiverAccess()
            }
        }
    }
}