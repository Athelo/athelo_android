package com.athelohealth.mobile.presentation.ui.caregiver.patientList

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToConnectFitbitScreen
import com.athelohealth.mobile.utils.routeToHome
import com.athelohealth.mobile.utils.routeToInvitationCode
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