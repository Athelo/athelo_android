package com.athelohealth.mobile.useCase.patients

import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.message.LocalMessage
import javax.inject.Inject

class SendGlobalMessageUseCase @Inject constructor(val appManager: AppManager) {

    suspend operator fun invoke(message: LocalMessage) {
        return appManager.sendGlobalMessage(message)
    }
}