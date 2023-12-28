package com.athelohealth.mobile.utils.fitbit

import android.content.Intent
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.network.repository.member.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class FitbitConnectionHelper @Inject constructor(private val repository: MemberRepository) {
    private val _state = MutableStateFlow<FitbitState>(FitbitState.Unknown)

    val state = _state.asStateFlow()

    suspend fun checkState(showError: Boolean = false) {
        val hasFitbitUserProfile =
            repository.runCatching { getMyProfile() }
                .getOrNull()?.results?.firstOrNull()?.hasFitbitUserProfile == true
        _state.emit(
            if (hasFitbitUserProfile) FitbitState.Connected
            else if (showError) FitbitState.Failure("")
            else FitbitState.Unknown
        )
    }

    suspend fun clearState() {
        _state.emit(FitbitState.Unknown)
    }

    suspend fun parseResponse(intent: Intent): Boolean {
        debugPrint("Parse response from fitbit: ${intent.data}")
        _state.emit(FitbitState.Unknown)
        return if (intent.data?.host?.contains("success") == true) {
            checkState(true)
            true
        } else if (intent.data?.host?.contains("failure") == true) {
            _state.emit(FitbitState.Failure("Something went wrong. Check Bluetooth connection!"))
            true
        } else false
    }
}

