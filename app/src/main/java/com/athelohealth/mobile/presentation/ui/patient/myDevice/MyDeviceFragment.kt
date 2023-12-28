package com.athelohealth.mobile.presentation.ui.patient.myDevice

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
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