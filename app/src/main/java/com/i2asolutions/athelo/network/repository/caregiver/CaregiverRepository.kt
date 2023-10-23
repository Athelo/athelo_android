package com.i2asolutions.athelo.network.repository.caregiver

import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.caregiver.CaregiverRelationshipDto
import com.i2asolutions.athelo.network.dto.caregiver.InvitationDto

interface CaregiverRepository {

    suspend fun removeCaregiver(id: Int): Boolean
    suspend fun loadCaregivers(nextUrl: String?): PageResponseDto<CaregiverRelationshipDto>
    suspend fun loadPendingInvitation(nextUrl: String?): PageResponseDto<InvitationDto>
    suspend fun cancelPendingInvitation(id: Int): Boolean

    suspend fun loadPatients(nextUrl: String?): PageResponseDto<CaregiverRelationshipDto>
    suspend fun removePatient(id: String): Boolean

    suspend fun sendInvitation(nickName: String, email: String, relation: String): Boolean
    suspend fun confirmInvitation(code: String): Boolean

}