package com.athelohealth.mobile.presentation.ui.share.appointment.appointmentList

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.PREF_KEY_SHOULD_LOAD_DATA
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToJoinAppointment
import com.athelohealth.mobile.utils.routeToMyProfile
import com.athelohealth.mobile.utils.routeToScheduleMyAppointment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppointmentFragment : BaseComposeFragment<AppointmentViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        AppointmentScreen(viewModel = viewModel)
    }

    override val viewModel: AppointmentViewModel by viewModels()

    @Inject
    lateinit var pref: SharedPreferences

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

    override fun onResume() {
        super.onResume()
        val shouldRefreshScreen = pref.getBoolean(PREF_KEY_SHOULD_LOAD_DATA, false)
        if (shouldRefreshScreen) {
            viewModel.getAppointments()
            //Remove the key once we get a result from it.
            pref.edit().remove("ShouldLoadAppointment").apply()
        }
    }
}