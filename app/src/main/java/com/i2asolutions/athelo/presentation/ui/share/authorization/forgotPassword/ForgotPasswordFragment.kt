package com.i2asolutions.athelo.presentation.ui.share.authorization.forgotPassword

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BaseComposeFragment<ForgotPasswordViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        ForgotPasswordScreen(viewModel = viewModel)
    }
    override val viewModel: ForgotPasswordViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                is ForgotPasswordEffect.DisplayMessage -> {
                    showAlertDialog(
                        message = it.message,
                        positiveButtonText = getString(R.string.OK),
                        positiveButtonListener = { dialog ->
                            dialog.dismiss()

                        }, title = getString(R.string.Info)
                    )
                }
                ForgotPasswordEffect.GoBack -> {
                    routeToBackScreen()
                }
            }
        }
    }
}