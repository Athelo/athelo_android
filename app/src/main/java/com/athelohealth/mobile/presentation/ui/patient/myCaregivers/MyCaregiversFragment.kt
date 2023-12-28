package com.athelohealth.mobile.presentation.ui.patient.myCaregivers

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToChat
import com.athelohealth.mobile.utils.routeToFindCaregiver
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