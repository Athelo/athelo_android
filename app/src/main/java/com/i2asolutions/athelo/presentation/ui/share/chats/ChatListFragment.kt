package com.i2asolutions.athelo.presentation.ui.share.chats

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToChat
import com.i2asolutions.athelo.utils.routeToMyProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseComposeFragment<ChatListViewModel>() {
    override val viewModel: ChatListViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        ChatListScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                is ChatListEffect.ShowConversation -> routeToChat(effect.conversationId)
                ChatListEffect.ShowMenuScreen -> openMenu()
                ChatListEffect.ShowMyProfileScreen -> routeToMyProfile()
            }
        }
    }

}