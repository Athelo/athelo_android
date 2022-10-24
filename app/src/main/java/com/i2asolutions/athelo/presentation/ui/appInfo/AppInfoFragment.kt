package com.i2asolutions.athelo.presentation.ui.appInfo

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToMail
import com.i2asolutions.athelo.utils.routeToWebView
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