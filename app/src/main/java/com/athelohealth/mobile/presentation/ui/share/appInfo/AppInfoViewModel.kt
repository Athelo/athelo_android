package com.athelohealth.mobile.presentation.ui.share.appInfo

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.AppInfoScreenType
import com.athelohealth.mobile.presentation.model.application.Application
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.application.LoadApplicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppInfoViewModel @Inject constructor(
    val loadApplication: LoadApplicationUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AppInfoEvent, AppInfoEffect, AppInfoViewState>(AppInfoViewState(isLoading = false, screenName = R.string.empty, text = "")) {

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    private val screenType: AppInfoScreenType =
        AppInfoFragmentArgs.fromSavedStateHandle(savedStateHandle).screenType

    override fun loadData() {
        launchRequest {
            notifyStateChange(currentState.copy(isLoading = true))
            val application = loadApplication()
            sendTextToUi(application)
        }
    }

    override fun handleEvent(event: AppInfoEvent) {
        when (event) {
            AppInfoEvent.BackClicked -> notifyEffectChanged(AppInfoEffect.GoBack)
            is AppInfoEvent.LinkClicked -> handleUrlEvent(event.url)
        }
    }

    private fun sendTextToUi(application: Application?) {
        if (application == null) {
            notifyStateChange(currentState.copy(isLoading = false, text = ""))
            return
        }
        val text = when (screenType) {
            AppInfoScreenType.PrivacyPolicy -> application.privacyPolicy
            AppInfoScreenType.TermsOfUse -> application.termsOfUse
            AppInfoScreenType.About -> application.aboutUs
        }
        val screenName = when (screenType) {
            AppInfoScreenType.PrivacyPolicy -> R.string.Privacy_Policy
            AppInfoScreenType.TermsOfUse -> R.string.Terms_of_Use
            AppInfoScreenType.About -> R.string.About_Us
        }
        notifyStateChange(
            currentState.copy(
                isLoading = false,
                screenName = screenName,
                text = text
            )
        )
    }

    private fun handleUrlEvent(url: String) {
        if (url.startsWith("mailto")) {
            notifyEffectChanged(AppInfoEffect.OpenEmail(url.replace("mailto:", "")))
        } else if (url.startsWith("http")) {
            notifyEffectChanged(AppInfoEffect.OpenUrl(url))
        }
    }
}