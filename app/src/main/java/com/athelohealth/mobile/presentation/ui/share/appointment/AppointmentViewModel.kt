package com.athelohealth.mobile.presentation.ui.share.appointment

import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(): BaseViewModel<AppointmentEvent, AppointmentEffect, AppointmentViewState>(AppointmentViewState()) {
    override fun pauseLoadingState() {

    }

    override fun loadData() {

    }
}