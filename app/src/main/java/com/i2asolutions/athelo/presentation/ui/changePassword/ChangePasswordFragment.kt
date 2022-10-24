package com.i2asolutions.athelo.presentation.ui.changePassword

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseComposeFragment<ChangePasswordViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        ChangePasswordScreen(viewModel = viewModel)
    }
    override val viewModel: ChangePasswordViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                ChangePasswordEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }
}