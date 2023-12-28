package com.athelohealth.mobile.presentation.ui.share.changePassword

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
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