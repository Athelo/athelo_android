package com.athelohealth.mobile.presentation.ui.share.chat

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToChat
import com.athelohealth.mobile.utils.routeToMyProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseComposeFragment<ChatViewModel>() {
    override val viewModel: ChatViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        ChatScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                is ChatEffect.ShowConversation -> routeToChat(effect.conversationId)
                ChatEffect.ShowMenuScreen -> openMenu()
                ChatEffect.ShowMyProfileScreen -> routeToMyProfile()
                ChatEffect.GoBack -> routeToBackScreen()
            }
        }
    }

}