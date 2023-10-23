package com.i2asolutions.athelo.presentation.ui.share.settings

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseComposeFragment<SettingsViewModel>() {

    override val viewModel: SettingsViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        SettingsScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                SettingsEffect.ShowPrevScreen -> routeToBackScreen()
                SettingsEffect.ShowAboutUsScreen -> routeToAboutScreen()
                SettingsEffect.ShowPersonalInformationScreen -> routeToEditProfile(true)
                SettingsEffect.ShowPrivacyPolicyScreen -> routeToPrivacyPolicyScreen()
                SettingsEffect.ShowTermsOfUseScreen -> routeToTermsOfUseScreen()
            }
        }
    }
}