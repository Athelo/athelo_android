package com.athelohealth.mobile.useCase.pack

import com.athelohealth.mobile.useCase.SetupPersonalConfigUseCase
import com.athelohealth.mobile.useCase.application.ShowPinUseCase
import com.athelohealth.mobile.useCase.common.CheckTutorialStateUseCase
import com.athelohealth.mobile.useCase.common.LoadEnumsUseCase
import com.athelohealth.mobile.useCase.member.*
import com.athelohealth.mobile.useCase.websocket.ConnectWebSocketUseCase
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