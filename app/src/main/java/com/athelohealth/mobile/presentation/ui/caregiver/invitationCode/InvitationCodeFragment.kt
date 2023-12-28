package com.athelohealth.mobile.presentation.ui.caregiver.invitationCode

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToAdditionalInformation
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToHome
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