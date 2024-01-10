package com.athelohealth.mobile.presentation.ui.share.tutorial

import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.tutorial.TutorialPageItem
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor() : BaseViewModel<TutorialEvent, TutorialEffect, TutorialViewState>(TutorialViewState(emptyList())) {
    init {
        notifyStateChange(
            TutorialViewState(
                listOf(
                    TutorialPageItem.FirstPage(
                        bigLogo = R.drawable.athelo_logo_with_text,
                        message = R.string.tutorial_intro_message,
                    ),
                    TutorialPageItem.TutorialPage(
                        title = R.string.tutorial_step1_title,
                        smallLogo = R.drawable.athelo_logo_with_text,
                        bigLogo = R.drawable.ic_welcome_1,
                    ),
                    TutorialPageItem.TutorialPage(
                        title = R.string.tutorial_step2_title,
                        smallLogo = R.drawable.athelo_logo_with_text,
                        bigLogo = R.drawable.ic_welcome_2,
                    ),
                    TutorialPageItem.TutorialPage(
                        title = R.string.tutorial_step3_title,
                        smallLogo = R.drawable.athelo_logo_with_text,
                        bigLogo = R.drawable.ic_welcome_3,
                    ),
                    TutorialPageItem.LastPage
                )
            )
        )
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {}

    override fun handleEvent(event: TutorialEvent) {
        when (event) {
            is TutorialEvent.UpdateScreen -> {
                if (event.currentPage is TutorialPageItem.LastPage)
                    selfBlockRun { notifyEffectChanged(TutorialEffect.ShowAuthorization) }
            }
        }
    }
}