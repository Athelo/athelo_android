package com.athelohealth.mobile.presentation.ui.share.appointment.appointmentList

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToJoinAppointment
import com.athelohealth.mobile.utils.routeToMyProfile
import com.athelohealth.mobile.utils.routeToScheduleMyAppointment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppointmentFragment : BaseComposeFragment<AppointmentViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        AppointmentScreen(viewModel = viewModel)
    }

    override val viewModel: AppointmentViewModel by viewModels()

    override fun setupObservers() {

        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                is AppointmentEffect.ShowMenuScreen -> openMenu()

                is AppointmentEffect.ShowMyProfileScreen -> routeToMyProfile()

                is AppointmentEffect.ShowScheduleMyAppointment -> {
                    routeToScheduleMyAppointment()
                }

                is AppointmentEffect.JoinAppointment -> routeToJoinAppointment(
                    effect.appointmentId, effect.sessionKey
                )
            }
        }
    }
}