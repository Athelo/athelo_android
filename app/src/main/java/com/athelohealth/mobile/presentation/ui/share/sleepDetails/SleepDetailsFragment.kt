package com.athelohealth.mobile.presentation.ui.share.sleepDetails

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToLostCaregiverAccess
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
                SleepDetailEffect.ShowLostCaregiverScreen -> routeToLostCaregiverAccess()
            }
        }
    }
}