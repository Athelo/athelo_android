package com.athelohealth.mobile.presentation.ui.share.appointment


import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.profile.ProfileItems
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState


data class AppointmentViewState(
    val initialized: Boolean = false,
    override val isLoading: Boolean = false,
    val user: User = User(),
    val buttons: List<ProfileItems> = emptyList(),
    val showLogoutOutPopup: Boolean = false,
    val showDeletePopup: Boolean = false,
) : BaseViewState

sealed interface AppointmentEvent : BaseEvent {

    object MenuClick : AppointmentEvent
    object MyProfileClick : AppointmentEvent
    object ScheduleMyAppointmentClick : AppointmentEvent
    data class ShowSuccessMessage(val msg: String): AppointmentEvent
    data class DeleteAppointment(val appointmentId: Int): AppointmentEvent
}

sealed interface AppointmentEffect : BaseEffect {
    object ShowMenuScreen : AppointmentEffect
    object ShowMyProfileScreen : AppointmentEffect
    object ShowScheduleMyAppointment : AppointmentEffect
}