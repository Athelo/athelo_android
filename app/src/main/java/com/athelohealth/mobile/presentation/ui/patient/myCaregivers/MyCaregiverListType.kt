package com.athelohealth.mobile.presentation.ui.patient.myCaregivers

sealed interface MyCaregiverListType {
    object MyCaregivers : MyCaregiverListType
    object PendingInvitation : MyCaregiverListType
}