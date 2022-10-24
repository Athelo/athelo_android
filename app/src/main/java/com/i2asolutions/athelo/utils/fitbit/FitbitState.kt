package com.i2asolutions.athelo.utils.fitbit

import com.i2asolutions.athelo.presentation.model.connectFitbit.ConnectFitbitScreenType

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