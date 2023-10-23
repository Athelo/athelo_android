package com.i2asolutions.athelo.useCase

import com.i2asolutions.athelo.presentation.model.member.Token
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.useCase.chat.LoadConversationsUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.useCase.member.StoreSessionUseCase
import com.i2asolutions.athelo.useCase.member.StoreUserEmailUseCase
import com.i2asolutions.athelo.useCase.member.StoreUserUseCase
import com.i2asolutions.athelo.useCase.websocket.ConnectWebSocketUseCase
import com.i2asolutions.athelo.utils.PreferenceHelper
import javax.inject.Inject

class SetupPersonalConfigUseCase @Inject constructor(
    private val preferenceHelper: PreferenceHelper,
    private val connectWebSocketUseCase: ConnectWebSocketUseCase,
    private val loadConversations: LoadConversationsUseCase,
    private val storeSessionUseCase: StoreSessionUseCase,
    private val storeUserUseCase: StoreUserUseCase,
    private val loadProfile: LoadMyProfileUseCase,
    private val storeUserEmailUseCase: StoreUserEmailUseCase,
) {

    suspend operator fun invoke(token: Token, username: String? = null): User? {
        storeSessionUseCase(token)
        storeUserEmailUseCase(username)
        connectWebSocketUseCase()
        val profile = loadProfile()
        storeUserUseCase(profile)
        preferenceHelper.showChatGroupLanding = preferenceHelper.showChatGroupLanding &&
                !runCatching { loadConversations().result.any { it.myConversation } }
                    .getOrDefault(false)
        return profile
    }

    suspend operator fun invoke(): User? {
        val profile = loadProfile()
        storeUserUseCase(profile)
        preferenceHelper.showChatGroupLanding = preferenceHelper.showChatGroupLanding &&
                !runCatching { loadConversations().result.any { it.myConversation } }.getOrDefault(
                    false
                )
        return profile
    }
}