package com.athelohealth.mobile.presentation.ui.share.messages

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToChat
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