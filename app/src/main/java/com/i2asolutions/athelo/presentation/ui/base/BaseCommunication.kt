package com.i2asolutions.athelo.presentation.ui.base

interface BaseEffect {
    object ShowAuthorizationScreen : BaseEffect
}

interface BaseViewState {
    val isLoading: Boolean
}

interface BaseEvent {
    object RefreshData : BaseEvent
    class DisplayMessage(val message: String) : BaseEvent
    class DisplayError(val error: String) : BaseEvent
    class DisplaySuccess(val success: String) : BaseEvent
}

sealed interface MessageState {
    val message: String

    @JvmInline
    value class NoMessageState(override val message: String = "") : MessageState

    @JvmInline
    value class ErrorMessageState(override val message: String) : MessageState

    @JvmInline
    value class SuccessMessageState(override val message: String) : MessageState

    @JvmInline
    value class NormalMessageState(override val message: String) : MessageState

    @JvmInline
    value class LowConnectivityMessageState(override val message: String) : MessageState
}
