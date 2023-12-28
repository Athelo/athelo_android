package com.athelohealth.mobile.presentation.ui.patient.addCaregiver

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindCaregiverFragment : BaseComposeFragment<FindCaregiverViewModel>() {

    override val viewModel: FindCaregiverViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        FindCaregiverScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                FindCaregiverEffect.ShowPrevScreen -> {
                    routeToBackScreen()
                }
            }
        }
    }
}