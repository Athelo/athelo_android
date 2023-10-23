package com.i2asolutions.athelo.presentation.ui.share.appInfo

import androidx.lifecycle.SavedStateHandle
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.AppInfoScreenType
import com.i2asolutions.athelo.presentation.model.application.Application
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.application.LoadApplicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppInfoViewModel @Inject constructor(
    val loadApplication: LoadApplicationUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AppInfoEvent, AppInfoEffect>() {
    private var currentState =
        AppInfoViewState(isLoading = false, screenName = R.string.empty, text = "")

    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    private val screenType: AppInfoScreenType =
        AppInfoFragmentArgs.fromSavedStateHandle(savedStateHandle).screenType

    override fun loadData() {
        launchRequest {
            notifyStateChanged(currentState.copy(isLoading = true))
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
            notifyStateChanged(currentState.copy(isLoading = false, text = ""))
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
        notifyStateChanged(
            currentState.copy(
                isLoading = false,
                screenName = screenName,
                text = text
            )
        )
    }

    private fun notifyStateChanged(newState: AppInfoViewState) {
        currentState = newState
        launchOnUI { _viewState.emit(currentState) }
    }

    private fun handleUrlEvent(url: String) {
        if (url.startsWith("mailto")) {
            notifyEffectChanged(AppInfoEffect.OpenEmail(url.replace("mailto:", "")))
        } else if (url.startsWith("http")) {
            notifyEffectChanged(AppInfoEffect.OpenUrl(url))
        }
    }
}