package com.i2asolutions.athelo.presentation.ui.share.messages

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToChat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : BaseComposeFragment<MessageViewModel>() {

    override val viewModel: MessageViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        MessageScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                is MessageEffect.ShowChatScreen -> routeToChat(
                    effect.conversation.conversationId,
                    false
                )
                MessageEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }
}