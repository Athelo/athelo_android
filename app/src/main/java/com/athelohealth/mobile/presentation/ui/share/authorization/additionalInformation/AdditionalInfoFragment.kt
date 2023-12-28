package com.athelohealth.mobile.presentation.ui.share.authorization.additionalInformation

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToConnectFitbitScreen
import com.athelohealth.mobile.utils.routeToHome
import com.athelohealth.mobile.utils.routeToSelectRole
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
                AdditionalInfoEffect.ShowConnectSmartWatchScreen -> routeToConnectFitbitScreen(
                    true
                )
                is AdditionalInfoEffect.ShowActAsScreen -> routeToSelectRole(
                    it.initFlow
                )
            }
        }
    }

}