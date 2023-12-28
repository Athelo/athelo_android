package com.athelohealth.mobile.network.repository.caregiver

import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.caregiver.CaregiverRelationshipDto
import com.athelohealth.mobile.network.dto.caregiver.InvitationDto

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