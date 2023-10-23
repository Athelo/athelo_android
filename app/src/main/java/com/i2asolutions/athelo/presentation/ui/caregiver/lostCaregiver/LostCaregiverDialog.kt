package com.i2asolutions.athelo.presentation.ui.caregiver.lostCaregiver

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.createComposeContainerView
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.utils.routeToSelectRole
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LostCaregiverDialog : BottomSheetDialogFragment() {

    val viewModel: LostCaregiverViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.FullscreenDialog).also {
            it.setCancelable(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createComposeContainerView(backgroundColorRes = android.R.color.transparent) {
        LostCaregiverScreen(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        viewModel.loadData()
        dialog?.setCancelable(false)
    }


    fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                LostCaregiverEffect.ShowRoleScreen -> routeToSelectRole(true)
            }
        }
    }
}