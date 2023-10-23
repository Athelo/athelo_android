package com.i2asolutions.athelo.useCase.pack

import com.i2asolutions.athelo.useCase.SetupPersonalConfigUseCase
import com.i2asolutions.athelo.useCase.application.ShowPinUseCase
import com.i2asolutions.athelo.useCase.common.CheckTutorialStateUseCase
import com.i2asolutions.athelo.useCase.common.LoadEnumsUseCase
import com.i2asolutions.athelo.useCase.member.*
import com.i2asolutions.athelo.useCase.websocket.ConnectWebSocketUseCase
import javax.inject.Inject

class SplashUseCases @Inject constructor(
//    val getInitialConversationsUseCase: GetInitialConversationsUseCase,
    val getUserStateUseCase: LoadUserStateUseCase,
    val userAuthorizationCheckUseCase: UserAuthorizationCheckUseCase,
//    val receivedFriendInvitationsUseCase: GetReceivedFriendInvitationsUseCase,
    val getEnumsUseCase: LoadEnumsUseCase,
    val showTutorial: CheckTutorialStateUseCase,
    val loadUserProfile: LoadMyProfileUseCase,
    val setupPersonalConfigUseCase: SetupPersonalConfigUseCase,
    val storeUser: StoreUserUseCase,
    val connectWebSocketUseCase: ConnectWebSocketUseCase,
    val verifyPin: CheckUserPinUseCase,
    val showPin: ShowPinUseCase,
)