package com.athelohealth.mobile.presentation.ui.share.heartRate

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToLostCaregiverAccess
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