package com.i2asolutions.athelo.useCase.patients

import com.i2asolutions.athelo.utils.app.AppManager
import com.i2asolutions.athelo.utils.message.LocalMessage
import javax.inject.Inject

class SendGlobalMessageUseCase @Inject constructor(val appManager: AppManager) {

    suspend operator fun invoke(message: LocalMessage) {
        return appManager.sendGlobalMessage(message)
    }
}