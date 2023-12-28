package com.athelohealth.mobile.presentation.ui.share.steps

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToLostCaregiverAccess
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