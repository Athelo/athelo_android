package com.i2asolutions.athelo.presentation.ui.patient.myCaregivers

import com.i2asolutions.athelo.presentation.model.caregiver.Caregiver
import com.i2asolutions.athelo.presentation.model.caregiver.Invitation
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.caregiver.CancelInvitationUseCase
import com.i2asolutions.athelo.useCase.caregiver.DeleteCaregiverUseCase
import com.i2asolutions.athelo.useCase.caregiver.LoadCaregiversUseCase
import com.i2asolutions.athelo.useCase.caregiver.LoadPendingInvitationsUseCase
import com.i2asolutions.athelo.useCase.chat.FindOrCreateConversationIdUseCase
import com.i2asolutions.athelo.utils.consts.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyCaregiversViewModel @Inject constructor(
    private val findOrCreateChat: FindOrCreateConversationIdUseCase,
    private val loadCaregivers: LoadCaregiversUseCase,
    private val deleteCaregiver: DeleteCaregiverUseCase,
    private val loadPendingInvitations: LoadPendingInvitationsUseCase,
    private val cancelInvitation: CancelInvitationUseCase,
) : BaseViewModel<MyCaregiversEvent, MyCaregiversEffect>() {
    private val data = mutableSetOf<Caregiver>()
    private val invitations = mutableSetOf<Invitation>()
    private var nextUrl: String? = null
    private var selectedType: MyCaregiverListType = MyCaregiverListType.MyCaregivers

    private var currentState = MyCaregiversViewState()

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()

    override fun loadData() {
        notifyStateChanged(currentState.copy(isLoading = nextUrl.isNullOrBlank()))
        launchRequest {
            if (nextUrl.isNullOrBlank()) {
                data.clear()
                invitations.clear()
            }
            when (selectedType) {
                MyCaregiverListType.MyCaregivers -> loadCaregivers()
                MyCaregiverListType.PendingInvitation -> loadInvitations()
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun handleEvent(event: MyCaregiversEvent) {
        when (event) {
            MyCaregiversEvent.AddCaregiverButtonClick -> notifyEffectChanged(MyCaregiversEffect.ShowInvitationScreen)
            MyCaregiversEvent.BackButtonClick -> notifyEffectChanged(MyCaregiversEffect.ShowPrevScreen)
            MyCaregiversEvent.CancelClick -> notifyStateChanged(
                currentState.copy(
                    showCaregiverMoreOption = null,
                    showCaregiverDeleteConfirmation = null,
                    showInvitationDeleteConfirmation = null,
                )
            )
            is MyCaregiversEvent.CaregiverClick -> {}
            is MyCaregiversEvent.DeleteCaregiverClick -> {
                notifyStateChanged(
                    currentState.copy(
                        showCaregiverMoreOption = null,
                        showCaregiverDeleteConfirmation = event.caregiver,
                    )
                )
            }
            is MyCaregiversEvent.DeleteCaregiverConfirmationClick -> {
                notifyStateChanged(
                    currentState.copy(
                        showCaregiverMoreOption = null,
                        showCaregiverDeleteConfirmation = null,
                        showInvitationDeleteConfirmation = null,
                    )
                )
                launchRequest {
                    if (deleteCaregiver(event.caregiver.id)) {
                        nextUrl = null
                        loadData()
                    }
                }
            }
            is MyCaregiversEvent.SendMessageClick -> {
                notifyStateChanged(
                    currentState.copy(
                        isLoading = true,
                        showCaregiverMoreOption = null,
                        showCaregiverDeleteConfirmation = null,
                        showInvitationDeleteConfirmation = null,
                    )
                )
                launchRequest {
                    val conversationId = findOrCreateChat(event.caregiver.id.toString())
                    notifyStateChanged(currentState.copy(isLoading = false))
                    if (conversationId != null) {
                        notifyEffectChanged(
                            MyCaregiversEffect.ShowCaregiverConversationScreen(
                                conversationId
                            )
                        )
                    } else {
                        errorMessage(Const.UNIVERSAL_ERROR_MESSAGE)
                    }
                }
            }
            is MyCaregiversEvent.ShowMoreOptionClick -> notifyStateChanged(
                currentState.copy(
                    showCaregiverMoreOption = event.caregiver
                )
            )
            is MyCaregiversEvent.ListTypeButtonClick -> {
                launchRequest {
                    nextUrl = null
                    selectedType = event.type
                    loadData()
                }
            }
            is MyCaregiversEvent.DeleteInvitationClick -> {
                notifyStateChanged(
                    currentState.copy(
                        showInvitationDeleteConfirmation = event.invitation,
                        showCaregiverMoreOption = null,
                        showCaregiverDeleteConfirmation = null
                    )
                )
            }
            is MyCaregiversEvent.DeleteInvitationConfirmationClick -> {
                notifyStateChanged(currentState.copy(isLoading = true))
                launchRequest {
                    val result = cancelInvitation(event.invitation.id)
                    if (result) {
                        nextUrl = null
                        loadData()
                    } else {
                        errorMessage(Const.UNIVERSAL_ERROR_MESSAGE)
                    }
                }
            }
            MyCaregiversEvent.LoadNextPage -> {
                loadData()
            }
        }
    }

    private suspend fun loadCaregivers() {
        val result = loadCaregivers(nextUrl)
        nextUrl = result.next
        if (result.result.isNotEmpty()) {
            data.addAll(result.result)
        }
        notifyStateChanged(
            currentState.copy(
                isLoading = false,
                caregivers = data.toList(),
                invitations = emptyList(),
                selectedType = MyCaregiverListType.MyCaregivers,
                canLoadMore = false,
                showCaregiverDeleteConfirmation = null,
                showCaregiverMoreOption = null,
                showInvitationDeleteConfirmation = null,
            )
        )
        delay(800)
        notifyStateChanged(currentState.copy(canLoadMore = !nextUrl.isNullOrBlank()))
    }

    private suspend fun loadInvitations() {
        val result = loadPendingInvitations(nextUrl)
        nextUrl = result.next
        if (result.result.isNotEmpty()) {
            invitations.addAll(result.result)
        }
        notifyStateChanged(
            currentState.copy(
                isLoading = false,
                caregivers = emptyList(),
                invitations = invitations.toList(),
                selectedType = MyCaregiverListType.PendingInvitation,
                showCaregiverDeleteConfirmation = null,
                showCaregiverMoreOption = null,
                showInvitationDeleteConfirmation = null,
                canLoadMore = false,
            )
        )
        delay(800)
        notifyStateChanged(currentState.copy(canLoadMore = !nextUrl.isNullOrBlank()))
    }

    private fun notifyStateChanged(newState: MyCaregiversViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}