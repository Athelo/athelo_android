package com.i2asolutions.athelo.useCase.patients

import com.i2asolutions.athelo.utils.app.AppManager
import com.i2asolutions.athelo.utils.message.LocalMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGlobalMessageUseCase @Inject constructor(val appManager: AppManager) {

    operator fun invoke(): Flow<LocalMessage> {
        return appManager.globalMessageFlow
    }
}