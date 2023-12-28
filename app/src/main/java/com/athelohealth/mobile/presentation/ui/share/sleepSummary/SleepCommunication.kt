package com.athelohealth.mobile.presentation.ui.share.sleepSummary

import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.patients.Patient
import com.athelohealth.mobile.presentation.model.sleep.SleepSummaryScreen.*
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class SleepViewState(
    override val isLoading: Boolean,
    val showNotConnected: Boolean = false,
    val currentUser: User = User(),
    val idealSleep: IdealSleep? = null,
    val sleepResult: SleepResult? = null,
    val sleepInformation: SleepInformation? = null,
    val patients: List<Patient> = emptyList(),
    val selectedPatient: Patient? = null,
) : BaseViewState

sealed interface SleepSummaryEvent : BaseEvent {
    data class ChangePatient(val patient: Patient) : SleepSummaryEvent
    class ReadArticleClick(val articleId: Int) : SleepSummaryEvent
    object MenuClick : SleepSummaryEvent
    object MyProfileClick : SleepSummaryEvent
    object ConnectSmartWatchClick : SleepSummaryEvent
    object RefreshData : SleepSummaryEvent
    object MoreClick : SleepSummaryEvent
}

sealed interface SleepEffect : BaseEffect {
    class ShowArticle(val articleId: Int) : SleepEffect
    object ShowMenuScreen : SleepEffect
    object ShowMyProfileScreen : SleepEffect
    object ShowConnectSmartWatchScreen : SleepEffect
    object ShowSleepDetailsScreen : SleepEffect
    object ShowLostCaregiverScreen : SleepEffect
}