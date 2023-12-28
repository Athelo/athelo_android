package com.athelohealth.mobile.presentation.ui.share.selectRole

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToCaregiverList
import com.athelohealth.mobile.utils.routeToPatientList

class SelectRoleFragment : BaseComposeFragment<SelectRoleViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        SelectRoleScreen(viewModel = viewModel)
    }

    override val viewModel: SelectRoleViewModel by viewModels()


    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                is SelectRoleEffect.ShowCaregiverListScreen -> routeToCaregiverList(effect.initialFlow)
                is SelectRoleEffect.ShowPatientListScreen -> routeToPatientList(effect.initialFlow)
                SelectRoleEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }

}