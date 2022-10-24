package com.i2asolutions.athelo.presentation.ui.sleepSummary

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToConnectFitbitScreen
import com.i2asolutions.athelo.utils.routeToMyProfile
import com.i2asolutions.athelo.utils.routeToNewsDetail
import com.i2asolutions.athelo.utils.routeToSleepDetails
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
                is SleepEffect.ShowArticle -> routeToNewsDetail(it.articleId)
                SleepEffect.ShowConnectSmartWatchScreen -> routeToConnectFitbitScreen(false)
                SleepEffect.ShowSleepDetailsScreen -> routeToSleepDetails()
            }
        }
    }
}