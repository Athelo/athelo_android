package com.i2asolutions.athelo.presentation.ui.share.messages

import com.i2asolutions.athelo.presentation.model.chat.PrivateConversation
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.caregiver.LoadCaregiversUseCase
import com.i2asolutions.athelo.useCase.caregiver.LoadPatientsUseCase
import com.i2asolutions.athelo.useCase.messages.LoadMessagesUseCase
import com.i2asolutions.athelo.utils.app.AppManager
import com.i2asolutions.athelo.utils.app.AppType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val loadMessages: LoadMessagesUseCase,
    private val loadPatientsUseCase: LoadPatientsUseCase,
    private val loadCaregiversUseCase: LoadCaregiversUseCase,
    private val appManager: AppManager,
) : BaseViewModel<MessageEvent, MessageEffect>() {
    private var nextUrl: String? = null
    private var nextUserUrl: String? = null
    private val messages: MutableList<PrivateConversation> = mutableListOf()
    private var currentState = MessageViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {
        notifyStateChanged(currentState.copy(isLoading = true))
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
            notifyStateChanged(currentState.copy(isLoading = false, messages = messages.result))
        }
    }

    private fun loadMessageForPatient() {
        launchRequest {
            messages.clear()
            val caregivers = loadCaregiversUseCase(nextUserUrl)
            nextUserUrl = caregivers.next
            val messages = loadMessages(nextUrl, caregivers.result.mapNotNull { it.id })
            nextUrl = messages.next
            notifyStateChanged(currentState.copy(isLoading = false, messages = messages.result))
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

    private fun notifyStateChanged(newState: MessageViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}