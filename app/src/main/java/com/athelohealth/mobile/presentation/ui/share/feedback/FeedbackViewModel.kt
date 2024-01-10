package com.athelohealth.mobile.presentation.ui.share.feedback

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.model.base.InputType
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.model.feedback.FeedbackScreenType
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.application.CreateFeedbackUseCase
import com.athelohealth.mobile.useCase.application.LoadFeedbackTopicUseCase
import com.athelohealth.mobile.utils.consts.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val loadFeedbackTopic: LoadFeedbackTopicUseCase,
    private val createFeedback: CreateFeedbackUseCase,
    savedStateHandle: SavedStateHandle
) :
    BaseViewModel<FeedbackEvent, FeedbackEffect, FeedbackViewState>(FeedbackViewState()) {
    private val type =
        FeedbackScreenType.values()[FeedbackFragmentArgs.fromSavedStateHandle(savedStateHandle).type]
    private var selectedTopic: EnumItem = EnumItem.EMPTY
    private var message: String = ""

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            val data = loadFeedbackTopic()
            initialSelectedOption(data)
            notifyStateChange(
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
        notifyStateChange(
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
                notifyStateChange(currentState.copy(isLoading = true))
                createFeedback(selectedTopic.id, message)
                message = ""
                initialSelectedOption(currentState.options)
                notifyStateChange(
                    currentState.copy(
                        isLoading = false,
                        selectedOption = selectedTopic,
                        message = message
                    )
                )
                successMessage("Thank you for your feedback!")
            }
        } else {
            notifyStateChange(currentState.copy(isLoading = false))
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
}