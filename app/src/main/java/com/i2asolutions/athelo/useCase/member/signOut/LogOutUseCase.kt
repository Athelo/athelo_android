package com.i2asolutions.athelo.useCase.member.signOut

import com.i2asolutions.athelo.useCase.websocket.DisconnectWebSocketUseCase
import com.i2asolutions.athelo.utils.app.AppManager
import com.i2asolutions.athelo.utils.app.AppType
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