package com.i2asolutions.athelo.presentation.ui.share.selectRole

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToCaregiverList
import com.i2asolutions.athelo.utils.routeToPatientList

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