package com.i2asolutions.athelo.presentation.ui.share.chat

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToChat
import com.i2asolutions.athelo.utils.routeToMyProfile
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