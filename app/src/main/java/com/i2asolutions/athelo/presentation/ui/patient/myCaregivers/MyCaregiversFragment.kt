package com.i2asolutions.athelo.presentation.ui.patient.myCaregivers

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToChat
import com.i2asolutions.athelo.utils.routeToFindCaregiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCaregiversFragment : BaseComposeFragment<MyCaregiversViewModel>() {

    override val viewModel: MyCaregiversViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        MyCaregiversScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                is MyCaregiversEffect.ShowCaregiverConversationScreen ->
                    routeToChat(effect.conversationId, false)
                MyCaregiversEffect.ShowInvitationScreen -> routeToFindCaregiver()
                MyCaregiversEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }
}