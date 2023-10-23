package com.i2asolutions.athelo.presentation.ui.share.feedback

import androidx.lifecycle.SavedStateHandle
import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.model.feedback.FeedbackScreenType
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.application.CreateFeedbackUseCase
import com.i2asolutions.athelo.useCase.application.LoadFeedbackTopicUseCase
import com.i2asolutions.athelo.utils.consts.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val loadFeedbackTopic: LoadFeedbackTopicUseCase,
    private val createFeedback: CreateFeedbackUseCase,
    savedStateHandle: SavedStateHandle
) :
    BaseViewModel<FeedbackEvent, FeedbackEffect>() {
    private val type =
        FeedbackScreenType.values()[FeedbackFragmentArgs.fromSavedStateHandle(savedStateHandle).type]
    private var currentState = FeedbackViewState()
    private var selectedTopic: EnumItem = EnumItem.EMPTY
    private var message: String = ""

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun loadData() {
        notifyStateChanged(currentState.copy(isLoading = true))
        launchRequest {
            val data = loadFeedbackTopic()
            initialSelectedOption(data)
            notifyStateChanged(
                currentState.copy(isLoading = false, options = data, selectedOption = selectedTopic)
            )
        }
    }

    private fun initialSelectedOption(data: List<EnumItem>) {
        selectedTopic =
            if (type == FeedbackScreenType.AskAthelo) data.firstOrNull { it.id == Const.QUESTION_ID || it.label == Const.QUESTION_LABEL }
                ?: EnumItem.EMPTY else EnumItem.EMPTY
    }

    override fun handleEvent(event: FeedbackEvent) {
        when (event) {
            FeedbackEvent.BackButtonClick -> notifyEffectChanged(FeedbackEffect.ShowPrevScreen)
            FeedbackEvent.SendButtonClick -> sendFeedback()
            is FeedbackEvent.InputValueChanged -> handleInputValue(event.inputType)
        }
    }

    private fun handleInputValue(
        inputType: InputType,
    ) {
        when (inputType) {
            is InputType.Text -> message = inputType.value
            is InputType.DropDown -> selectedTopic =
                currentState.options.firstOrNull { it.id == inputType.value }
                    ?: EnumItem.EMPTY
            else -> {/*Ignore other option*/
            }
        }
        notifyStateChanged(
            currentState.copy(
                enableSendButton = validate(),
                selectedOption = selectedTopic,
                message = message
            )
        )
    }

    private fun sendFeedback() {
        if (validate()) {
            launchRequest {
                notifyStateChanged(currentState.copy(isLoading = true))
                createFeedback(selectedTopic.id, message)
                message = ""
                initialSelectedOption(currentState.options)
                notifyStateChanged(
                    currentState.copy(
                        isLoading = false,
                        selectedOption = selectedTopic,
                        message = message
                    )
                )
                successMessage("Thank you for your feedback!")
            }
        } else {
            notifyStateChanged(currentState.copy(isLoading = false))
            val message = buildString {
                if (!validateTopicSelection()) {
                    append("Please select Topic before sending feedback.")
                    append("\n")
                }
                if (!validateMessage()) {
                    append("Please enter message.")
                }
            }
            if (message.isNotBlank())
                errorMessage(message)
        }
    }

    private fun validate() = validateTopicSelection() && validateMessage()

    private fun validateMessage() = message.isNotBlank()

    private fun validateTopicSelection() = selectedTopic != EnumItem.EMPTY

    private fun notifyStateChanged(newState: FeedbackViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}