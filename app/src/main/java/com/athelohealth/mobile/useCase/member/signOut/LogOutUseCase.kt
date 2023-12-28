package com.athelohealth.mobile.useCase.member.signOut

import com.athelohealth.mobile.useCase.websocket.DisconnectWebSocketUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val clearUserInformationUseCase: ClearUserInformationUseCase,
    private val clearPersonalInformationUseCase: ClearPersonalInformationUseCase,
    private val clearConnectionStateUseCase: ClearConnectionStateUseCase,
    private val disconnectWebSockets: DisconnectWebSocketUseCase,
    private val appManager: AppManager,
) {

    suspend operator fun invoke() {
        clearUserInformationUseCase()
        clearPersonalInformationUseCase()
        clearConnectionStateUseCase()
        disconnectWebSockets()
        appManager.changeAppType(AppType.Unknown)
    }
}