package com.athelohealth.mobile.presentation.ui.containerWithTab

import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

interface TabsEvent : BaseEvent

interface TabsEffect : BaseEffect

data class TabsViewState(
    override val isLoading: Boolean = false
) : BaseViewState