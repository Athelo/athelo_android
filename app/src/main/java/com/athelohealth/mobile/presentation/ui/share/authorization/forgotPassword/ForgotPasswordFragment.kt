package com.athelohealth.mobile.presentation.ui.share.authorization.forgotPassword

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
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