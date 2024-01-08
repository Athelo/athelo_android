package com.athelohealth.mobile.utils.app

import android.content.Context
import androidx.core.content.edit
import com.athelohealth.mobile.utils.message.LocalMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber

private const val APP_TYPE_KEY = "selectedType"

private const val CURRENT_PATIENT_ID = "currentSelectedPatient"

class AppManager(context: Context) {
    private val preferences = context.getSharedPreferences(".appConfig", Context.MODE_PRIVATE)
    private var currentType: AppType = AppType.Patient

    private val _currentType = MutableStateFlow(currentType)

    val appType: StateFlow<AppType> = _currentType
    private var patientId: String? = ""

    private val _globalMessageFlow = MutableSharedFlow<LocalMessage>()

    val globalMessageFlow = _globalMessageFlow.asSharedFlow()

    init {
        loadType()
    }

    suspend fun sendGlobalMessage(localMessage: LocalMessage) {
        Timber.d("Message send: $localMessage")
        _globalMessageFlow.emit(localMessage)
    }

    fun changeAppType(appType: AppType) {
        currentType = AppType.Patient
        saveType()
        _currentType.tryEmit(currentType)
    }

    private fun saveType() {
        preferences.edit {
            putInt(APP_TYPE_KEY, currentType.type)
            putString(CURRENT_PATIENT_ID, currentType.patientId)
        }
    }

    private fun loadType() {
        val patientId = preferences.getString(CURRENT_PATIENT_ID, "")
//        currentType = preferences.getInt(APP_TYPE_KEY, -1).toAppType(patientId ?: "")
        currentType = AppType.Patient
        _currentType.tryEmit(currentType)
    }

    fun changePatientId(userId: String?) {
        if (userId != null) {
            changeAppType(AppType.Caregiver(userId))
        }
    }
}