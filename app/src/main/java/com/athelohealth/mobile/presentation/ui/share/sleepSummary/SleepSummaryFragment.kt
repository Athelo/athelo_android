package com.athelohealth.mobile.presentation.ui.share.sleepSummary

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SleepSummaryFragment : BaseComposeFragment<SleepSummaryViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        SleepSummaryScreen(viewModel = viewModel)
    }
    override val viewModel: SleepSummaryViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                SleepEffect.ShowMenuScreen -> openMenu()
                SleepEffect.ShowMyProfileScreen -> routeToMyProfile()
                is SleepEffect.ShowArticle -> routeToNewsDetail(it.articleId.toString())
                SleepEffect.ShowConnectSmartWatchScreen -> routeToConnectFitbitScreen(false)
                SleepEffect.ShowSleepDetailsScreen -> routeToSleepDetails()
                SleepEffect.ShowLostCaregiverScreen -> routeToLostCaregiverAccess()
            }
        }
    }
}