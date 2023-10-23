package com.i2asolutions.athelo.presentation.ui.caregiver.patientList

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToConnectFitbitScreen
import com.i2asolutions.athelo.utils.routeToHome
import com.i2asolutions.athelo.utils.routeToInvitationCode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientListFragment : BaseComposeFragment<PatientListViewModel>() {

    override val viewModel: PatientListViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        PatientListScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                PatientListEffect.ShowInvitationCodeScreen -> routeToInvitationCode()
                PatientListEffect.ShowPrevScreen -> routeToBackScreen()
                PatientListEffect.ShowHomeScreen -> routeToHome()
                PatientListEffect.ShowSmartWatchScreen -> routeToConnectFitbitScreen(true)
            }
        }
    }
}