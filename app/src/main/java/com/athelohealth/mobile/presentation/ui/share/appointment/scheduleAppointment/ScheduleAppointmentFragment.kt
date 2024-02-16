package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.PREF_KEY_SHOULD_LOAD_DATA
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleAppointmentFragment : BaseComposeFragment<ScheduleAppointmentViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        ScheduleMyAppointment(viewModel)
    }

    override val viewModel: ScheduleAppointmentViewModel by viewModels()

    @Inject lateinit var prefEditor: SharedPreferences.Editor

    override fun setupObservers() {

        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when(effect) {
                is ScheduleAppointmentEffect.ShowPrevScreen -> {
                    prefEditor.putBoolean(PREF_KEY_SHOULD_LOAD_DATA, effect.shouldLoadAppointments).apply()
                    routeToBackScreen()
                }
                is ScheduleAppointmentEffect.ShowSuccessMessage -> {
                    viewModel.displaySuccessMessage(effect.msg)
                }
            }
        }
    }
}