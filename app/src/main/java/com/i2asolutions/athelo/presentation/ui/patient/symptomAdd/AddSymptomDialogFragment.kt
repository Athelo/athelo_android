package com.i2asolutions.athelo.presentation.ui.patient.symptomAdd

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.createComposeContainerView
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.model.health.Symptom
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSymptomDialogFragment : BottomSheetDialogFragment() {
    private val viewModel: AddSymptomViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.FullscreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createComposeContainerView(backgroundColorRes = android.R.color.transparent) {
        AddSymptomScreen(viewModel = viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        viewModel.loadData()
    }

    private fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                is com.i2asolutions.athelo.presentation.ui.patient.symptomAdd.AddSymptomEffect.ShowPrevScreen -> {
                    it.symptom?.let {
                        sendDataBack(it)
                    }
                    dismissAllowingStateLoss()
                }
            }
        }
    }

    private fun sendDataBack(selected: Symptom) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "created_symptom",
            selected
        )
    }
}