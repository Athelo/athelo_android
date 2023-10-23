package com.i2asolutions.athelo.presentation.ui.share.authorization.signUpWithEmail

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToAdditionalInformation
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToPrivacyPolicyScreen
import com.i2asolutions.athelo.utils.routeToTermsOfUseScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseComposeFragment<SignUpViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        SignUpScreen(viewModel = viewModel)
    }
    override val viewModel: SignUpViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                SignUpEffect.GoBack -> routeToBackScreen()
                SignUpEffect.ShowAdditionalInfoScreen -> routeToAdditionalInformation()
                SignUpEffect.ShowPPScreen -> routeToPrivacyPolicyScreen()
                SignUpEffect.ShowToSScreen -> routeToTermsOfUseScreen()
            }
        }
    }
}