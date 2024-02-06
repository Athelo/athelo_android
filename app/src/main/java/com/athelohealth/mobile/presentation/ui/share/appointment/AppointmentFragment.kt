package com.athelohealth.mobile.presentation.ui.share.appointment

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToMyProfile
import com.athelohealth.mobile.utils.routeToScheduleMyAppointment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AppointmentFragment : BaseComposeFragment<AppointmentViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        AppointmentScreen(viewModel = viewModel)
    }

    override val viewModel: AppointmentViewModel by viewModels()

    override fun setupObservers() {

        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                AppointmentEffect.ShowMenuScreen -> openMenu()

                AppointmentEffect.ShowMyProfileScreen -> routeToMyProfile()

                AppointmentEffect.ShowScheduleMyAppointment -> {
                    routeToScheduleMyAppointment()
                }

                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume: AppointmentFragment")
    }
}