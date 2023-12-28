package com.athelohealth.mobile.presentation.ui.patient.caregiverList

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToConnectFitbitScreen
import com.athelohealth.mobile.utils.routeToFindCaregiver
import com.athelohealth.mobile.utils.routeToHome
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