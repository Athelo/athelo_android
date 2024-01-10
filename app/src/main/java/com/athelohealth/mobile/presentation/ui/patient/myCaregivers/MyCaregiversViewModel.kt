package com.athelohealth.mobile.presentation.ui.patient.myCaregivers

import com.athelohealth.mobile.presentation.model.caregiver.Caregiver
import com.athelohealth.mobile.presentation.model.caregiver.Invitation
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.caregiver.CancelInvitationUseCase
import com.athelohealth.mobile.useCase.caregiver.DeleteCaregiverUseCase
import com.athelohealth.mobile.useCase.caregiver.LoadCaregiversUseCase
import com.athelohealth.mobile.useCase.caregiver.LoadPendingInvitationsUseCase
import com.athelohealth.mobile.useCase.chat.FindOrCreateConversationIdUseCase
import com.athelohealth.mobile.utils.consts.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class MyCaregiversViewModel @Inject constructor(
    private val findOrCreateChat: FindOrCreateConversationIdUseCase,
    private val loadCaregivers: LoadCaregiversUseCase,
    private val deleteCaregiver: DeleteCaregiverUseCase,
    private val loadPendingInvitations: LoadPendingInvitationsUseCase,
    private val cancelInvitation: CancelInvitationUseCase,
) : BaseViewModel<MyCaregiversEvent, MyCaregiversEffect, MyCaregiversViewState>(MyCaregiversViewState()) {
    private val data = mutableSetOf<Caregiver>()
    private val invitations = mutableSetOf<Invitation>()
    private var nextUrl: String? = null
    private var selectedType: MyCaregiverListType = MyCaregiverListType.MyCaregivers

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = nextUrl.isNullOrBlank()))
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
        notifyStateChange(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun handleEvent(event: MyCaregiversEvent) {
        when (event) {
            MyCaregiversEvent.AddCaregiverButtonClick -> notifyEffectChanged(MyCaregiversEffect.ShowInvitationScreen)
            MyCaregiversEvent.BackButtonClick -> notifyEffectChanged(MyCaregiversEffect.ShowPrevScreen)
            MyCaregiversEvent.CancelClick -> notifyStateChange(
                currentState.copy(
                    showCaregiverMoreOption = null,
                    showCaregiverDeleteConfirmation = null,
                    showInvitationDeleteConfirmation = null,
                )
            )
            is MyCaregiversEvent.CaregiverClick -> {}
            is MyCaregiversEvent.DeleteCaregiverClick -> {
                notifyStateChange(
                    currentState.copy(
                        showCaregiverMoreOption = null,
                        showCaregiverDeleteConfirmation = event.caregiver,
                    )
                )
            }
            is MyCaregiversEvent.DeleteCaregiverConfirmationClick -> {
                notifyStateChange(
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
                notifyStateChange(
                    currentState.copy(
                        isLoading = true,
                        showCaregiverMoreOption = null,
                        showCaregiverDeleteConfirmation = null,
                        showInvitationDeleteConfirmation = null,
                    )
                )
                launchRequest {
                    val conversationId = findOrCreateChat(event.caregiver.id.toString())
                    notifyStateChange(currentState.copy(isLoading = false))
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
            is MyCaregiversEvent.ShowMoreOptionClick -> notifyStateChange(
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
                notifyStateChange(
                    currentState.copy(
                        showInvitationDeleteConfirmation = event.invitation,
                        showCaregiverMoreOption = null,
                        showCaregiverDeleteConfirmation = null
                    )
                )
            }
            is MyCaregiversEvent.DeleteInvitationConfirmationClick -> {
                notifyStateChange(currentState.copy(isLoading = true))
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
        notifyStateChange(
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
        notifyStateChange(currentState.copy(canLoadMore = !nextUrl.isNullOrBlank()))
    }

    private suspend fun loadInvitations() {
        val result = loadPendingInvitations(nextUrl)
        nextUrl = result.next
        if (result.result.isNotEmpty()) {
            invitations.addAll(result.result)
        }
        notifyStateChange(
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
        notifyStateChange(currentState.copy(canLoadMore = !nextUrl.isNullOrBlank()))
    }
}