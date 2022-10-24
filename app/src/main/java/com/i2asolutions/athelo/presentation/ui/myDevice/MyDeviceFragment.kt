package com.i2asolutions.athelo.presentation.ui.myDevice

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDeviceFragment : BaseComposeFragment<MyDeviceViewModel>() {

    override val viewModel: MyDeviceViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        MyDeviceScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                MyDeviceEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }
}