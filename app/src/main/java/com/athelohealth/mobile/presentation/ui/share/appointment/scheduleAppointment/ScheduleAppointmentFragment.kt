package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.presentation.ui.patient.wellbeing.WellbeingEffect
import com.athelohealth.mobile.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleAppointmentFragment : BaseComposeFragment<ScheduleAppointmentViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        ScheduleMyAppointment(viewModel)
    }

    override val viewModel: ScheduleAppointmentViewModel by viewModels()

    override fun setupObservers() {

        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when(effect) {
                is ScheduleAppointmentEffect.ShowPrevScreen -> routeToBackScreen()
                is ScheduleAppointmentEffect.ShowSuccessMessage -> {
                    viewModel.displaySuccessMessage(effect.msg)
                }
                else -> {}
            }
        }
    }
}