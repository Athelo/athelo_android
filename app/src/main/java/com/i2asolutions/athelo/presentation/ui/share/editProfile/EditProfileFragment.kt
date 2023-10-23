package com.i2asolutions.athelo.presentation.ui.share.editProfile

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.github.dhaval2404.imagepicker.ImagePicker
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseComposeFragment<EditProfileViewModel>() {
    override val viewModel: EditProfileViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        EditProfileScreen(viewModel)
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                viewModel.handleEvent(data?.data?.let {
                    EditProfileEvent.ImageSelected(it)
                } ?: EditProfileEvent.HideProgress)
            } else {
                viewModel.handleEvent(EditProfileEvent.HideProgress)
            }
        }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            handleEffect(it)
        }
    }

    private fun handleEffect(effect: EditProfileEffect) {
        when (effect) {
            EditProfileEffect.ShowImagePicker -> openImagePicker()
            EditProfileEffect.ShowResetPasswordScreen -> showToBeImplementedMessage()
            EditProfileEffect.ShowPrevScreen -> routeToBackScreen()
        }
    }


    @Suppress("unused_parameter")
    fun openImagePicker() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(width = 1080, height = 1080)
            .setImageProviderInterceptor { viewModel.handleEvent(EditProfileEvent.ShowProgress) }
            .createIntent { intent -> startForProfileImageResult.launch(intent) }
    }
}