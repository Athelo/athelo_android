package com.athelohealth.mobile.presentation.ui.share.hrv

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToLostCaregiverAccess
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