package com.i2asolutions.athelo.presentation.ui.caregiver.invitationCode

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToAdditionalInformation
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToHome
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvitationCodeFragment : BaseComposeFragment<InvitationCodeViewModel>() {

    override val viewModel: InvitationCodeViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        InvitationCodeScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                InvitationCodeEffect.ShowMainScreen -> routeToHome()
                InvitationCodeEffect.ShowAdditionalInfoScreen -> routeToAdditionalInformation()
                is InvitationCodeEffect.ShowPrevScreen -> {
                    routeToBackScreen()
                }
            }
        }
    }
}