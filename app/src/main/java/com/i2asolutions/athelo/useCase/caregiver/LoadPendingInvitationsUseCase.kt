package com.i2asolutions.athelo.useCase.caregiver

import com.i2asolutions.athelo.network.repository.caregiver.CaregiverRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.caregiver.Invitation
import com.i2asolutions.athelo.presentation.model.enums.Enums
import javax.inject.Inject

class LoadPendingInvitationsUseCase @Inject constructor(
    private val repository: CaregiverRepository,
    private val enums: Enums
) {

    suspend operator fun invoke(nextUrl: String?): PageResponse<Invitation> {
        return repository.loadPendingInvitation(nextUrl).toPageResponse { invitation ->
            invitation.toInvitation(enums.caregiverRelations)
        }
    }
}