package com.athelohealth.mobile.useCase

import com.athelohealth.mobile.presentation.model.member.Token
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.useCase.chat.LoadConversationsUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.StoreSessionUseCase
import com.athelohealth.mobile.useCase.member.StoreUserEmailUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.useCase.websocket.ConnectWebSocketUseCase
import com.athelohealth.mobile.utils.PreferenceHelper
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