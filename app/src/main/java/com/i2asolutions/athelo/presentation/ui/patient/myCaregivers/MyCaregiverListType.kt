package com.i2asolutions.athelo.presentation.ui.patient.myCaregivers

sealed interface MyCaregiverListType {
    object MyCaregivers : MyCaregiverListType
    object PendingInvitation : MyCaregiverListType
}