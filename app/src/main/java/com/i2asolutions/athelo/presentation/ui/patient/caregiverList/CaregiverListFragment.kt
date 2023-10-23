package com.i2asolutions.athelo.presentation.ui.patient.caregiverList

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToConnectFitbitScreen
import com.i2asolutions.athelo.utils.routeToFindCaregiver
import com.i2asolutions.athelo.utils.routeToHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CaregiverListFragment : BaseComposeFragment<CaregiverListViewModel>() {

    override val viewModel: CaregiverListViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        CaregiverListScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                CaregiverListEffect.ShowInvitationScreen -> routeToFindCaregiver()
                CaregiverListEffect.ShowPrevScreen -> routeToBackScreen()
                CaregiverListEffect.ShowHomeScreen -> routeToHome()
                is CaregiverListEffect.ShowRemoveConfirmationScreen -> showAlertDialog(
                    title = getString(R.string.Confirmation),
                    message = getString(R.string.confirmation_delete_caregiver),
                    positiveButtonText = getString(R.string.Delete),
                    positiveButtonListener = {
                        viewModel.handleEvent(CaregiverListEvent.ConfirmationDeleteClick(effect.caregiver))
                    },
                    negativeButtonText = getString(R.string.Cancel),
                    negativeButtonListener = {

                    }
                )
                CaregiverListEffect.ShowSmartWatchScreen -> routeToConnectFitbitScreen(true)
            }
        }
    }
}