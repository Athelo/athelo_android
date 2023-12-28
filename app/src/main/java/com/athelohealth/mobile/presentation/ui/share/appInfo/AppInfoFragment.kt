package com.athelohealth.mobile.presentation.ui.share.appInfo

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToMail
import com.athelohealth.mobile.utils.routeToWebView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppInfoFragment : BaseComposeFragment<AppInfoViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        AppInfoScreen(viewModel = viewModel)
    }
    override val viewModel: AppInfoViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                AppInfoEffect.GoBack -> routeToBackScreen()
                is AppInfoEffect.OpenEmail -> routeToMail(it.email)
                is AppInfoEffect.OpenUrl -> routeToWebView(it.url)
            }
        }
    }
}