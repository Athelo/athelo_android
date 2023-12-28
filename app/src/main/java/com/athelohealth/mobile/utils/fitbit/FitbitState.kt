package com.athelohealth.mobile.utils.fitbit

import com.athelohealth.mobile.presentation.model.connectFitbit.ConnectFitbitScreenType

sealed interface FitbitState {
    object Unknown : FitbitState
    object Connected : FitbitState
    data class Failure(val message: String) : FitbitState
}

fun FitbitState.toScreenType() = when (this) {
    FitbitState.Connected -> ConnectFitbitScreenType.Success
    is FitbitState.Failure -> ConnectFitbitScreenType.Error
    FitbitState.Unknown -> ConnectFitbitScreenType.Landing
}