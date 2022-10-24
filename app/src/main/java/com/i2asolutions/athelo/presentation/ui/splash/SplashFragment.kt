package com.i2asolutions.athelo.presentation.ui.splash

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToAdditionalInformation
import com.i2asolutions.athelo.utils.routeToAuthorization
import com.i2asolutions.athelo.utils.routeToHome
import com.i2asolutions.athelo.utils.routeToTutorial
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseComposeFragment<SplashViewModel>() {
    override val viewModel: SplashViewModel by viewModels()
    override val composeContent = @Composable {
        SplashScreen(viewModel = viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            observeEffects(effect)
        }
    }

    private fun observeEffects(effect: SplashEffect) = when (effect) {
        SplashEffect.CloseApp -> requireActivity().finish()
        SplashEffect.ShowAuthorizationScreen -> routeToAuthorization()
        SplashEffect.ShowHomeScreen -> routeToHome()
        SplashEffect.ShowTutorialScreen -> routeToTutorial()
        SplashEffect.ShowAdditionalInformationScreen -> routeToAdditionalInformation()
    }
}