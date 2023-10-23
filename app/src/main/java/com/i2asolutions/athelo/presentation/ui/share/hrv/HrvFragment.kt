package com.i2asolutions.athelo.presentation.ui.share.hrv

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToLostCaregiverAccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HrvFragment : BaseComposeFragment<HrvViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        HrvScreen(viewModel = viewModel)
    }
    override val viewModel: HrvViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                HrvEffect.GoBack -> routeToBackScreen()
                HrvEffect.ShowSelectRoleScreen -> routeToLostCaregiverAccess()
            }
        }
    }
}