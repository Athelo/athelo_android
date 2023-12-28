package com.athelohealth.mobile.useCase.patients

import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.message.LocalMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGlobalMessageUseCase @Inject constructor(val appManager: AppManager) {

    operator fun invoke(): Flow<LocalMessage> {
        return appManager.globalMessageFlow
    }
}