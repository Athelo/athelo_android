package com.i2asolutions.athelo.presentation.ui.sleepDetails

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SleepDetailsFragment : BaseComposeFragment<SleepDetailsViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        SleepDetailsScreen(viewModel = viewModel)
    }
    override val viewModel: SleepDetailsViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                SleepDetailEffect.GoBack -> routeToBackScreen()
            }
        }
    }
}