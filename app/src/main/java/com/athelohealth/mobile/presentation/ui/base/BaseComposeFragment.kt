package com.athelohealth.mobile.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import com.athelohealth.mobile.extensions.createComposeContainerView

abstract class BaseComposeFragment<VM : BaseViewModel<*, *>> : BaseFragment<VM>() {
    abstract val composeContent: @Composable () -> Unit

    override fun createContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createComposeContainerView(content = composeContent)


}