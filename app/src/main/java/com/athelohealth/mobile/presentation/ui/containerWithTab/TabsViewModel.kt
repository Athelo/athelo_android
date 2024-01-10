package com.athelohealth.mobile.presentation.ui.containerWithTab

import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor() : BaseViewModel<TabsEvent, TabsEffect, TabsViewState>(TabsViewState()) {
    override fun pauseLoadingState() {}

    override fun loadData() {}

}