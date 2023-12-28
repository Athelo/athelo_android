package com.athelohealth.mobile.presentation.ui.share.activity

import com.athelohealth.mobile.presentation.model.activity.ActivityScreen
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class ActivityViewState(
    override val isLoading: Boolean,
    val showNotConnected: Boolean = false,
    val currentUser: User = User(),
    val topHint: String = "",
    val stepsInformation: ActivityScreen.Steps? = null,
    val activityInformation: ActivityScreen.Activity? = null,
    val heartRateInformation: ActivityScreen.HeartRate? = null,
    val hrvInformation: ActivityScreen.HeartRateVariability? = null,
    val patients: List<Patient> = emptyList(),
    val selectedPatient: Patient? = null,
) : BaseViewState

sealed interface ActivityEvent : BaseEvent {
    data class ChangePatient(val patient: Patient) : ActivityEvent
    object MenuClick : ActivityEvent
    object MyProfileClick : ActivityEvent
    object ConnectSmartWatchClick : ActivityEvent
    object RefreshData : ActivityEvent
    object StepsClick : ActivityEvent
    object ActivityClick : ActivityEvent
    object HeartRateClick : ActivityEvent
    object HrvClick : ActivityEvent
}

sealed interface ActivityEffect : BaseEffect {
    object ShowMenuScreen : ActivityEffect
    object ShowMenu : ActivityEffect
    object ShowConnectSmartWatchScreen : ActivityEffect
    object ShowMyProfileScreen : ActivityEffect
    object ShowStepsScreen : ActivityEffect
    object ShowActivityScreen : ActivityEffect
    object ShowHeartRateScreen : ActivityEffect
    object ShowHrvScreen : ActivityEffect
    object ShowSelectRoleScreen : ActivityEffect
}