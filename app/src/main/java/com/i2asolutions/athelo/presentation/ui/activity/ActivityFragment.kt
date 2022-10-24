package com.i2asolutions.athelo.presentation.ui.activity

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.*
import com.i2asolutions.athelo.utils.routeToActivityHrv
import com.i2asolutions.athelo.utils.routeToActivitySteps
import com.i2asolutions.athelo.utils.routeToConnectFitbitScreen
import com.i2asolutions.athelo.utils.routeToMyProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityFragment : BaseComposeFragment<ActivityViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        ActivityScreen(viewModel = viewModel)
    }
    override val viewModel: ActivityViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                ActivityEffect.ShowMenuScreen -> openMenu()
                ActivityEffect.ShowActivityScreen -> routeToActivityExercise()
                ActivityEffect.ShowConnectSmartWatchScreen -> routeToConnectFitbitScreen(false)
                ActivityEffect.ShowHeartRateScreen -> routeToActivityHeartRate()
                ActivityEffect.ShowHrvScreen -> routeToActivityHrv()
                ActivityEffect.ShowMenu -> openMenu()
                ActivityEffect.ShowMyProfileScreen -> routeToMyProfile()
                ActivityEffect.ShowStepsScreen -> routeToActivitySteps()
            }
        }
    }
}