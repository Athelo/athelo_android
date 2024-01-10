package com.athelohealth.mobile.presentation.ui.share.messages

import com.athelohealth.mobile.presentation.model.chat.PrivateConversation
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.caregiver.LoadCaregiversUseCase
import com.athelohealth.mobile.useCase.caregiver.LoadPatientsUseCase
import com.athelohealth.mobile.useCase.messages.LoadMessagesUseCase
import com.athelohealth.mobile.utils.app.AppManager
import com.athelohealth.mobile.utils.app.AppType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val loadMessages: LoadMessagesUseCase,
    private val loadPatientsUseCase: LoadPatientsUseCase,
    private val loadCaregiversUseCase: LoadCaregiversUseCase,
    private val appManager: AppManager,
) : BaseViewModel<MessageEvent, MessageEffect, MessageViewState>(MessageViewState()) {
    private var nextUrl: String? = null
    private var nextUserUrl: String? = null
    private val messages: MutableList<PrivateConversation> = mutableListOf()

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        if (appManager.appType.value::class.java == AppType.Caregiver::class.java) loadMessageForCaregiver()
        else loadMessageForPatient()
    }

    private fun loadMessageForCaregiver() {
        launchRequest {
            messages.clear()
            val patients = loadPatientsUseCase(nextUserUrl)
            nextUserUrl = patients.next
            val messages = loadMessages(nextUrl, patients.result.mapNotNull { it.userId.toInt() })
            nextUrl = messages.next
            notifyStateChange(currentState.copy(isLoading = false, messages = messages.result))
        }
    }

    private fun loadMessageForPatient() {
        launchRequest {
            messages.clear()
            val caregivers = loadCaregiversUseCase(nextUserUrl)
            nextUserUrl = caregivers.next
            val messages = loadMessages(nextUrl, caregivers.result.mapNotNull { it.id })
            nextUrl = messages.next
            notifyStateChange(currentState.copy(isLoading = false, messages = messages.result))
        }
    }

    override fun handleEvent(event: MessageEvent) {
        when (event) {
            MessageEvent.BackButtonClick -> notifyEffectChanged(MessageEffect.ShowPrevScreen)
            is MessageEvent.ConversationItemClick -> notifyEffectChanged(
                MessageEffect.ShowChatScreen(
                    event.conversation
                )
            )
        }
    }

}