package com.athelohealth.mobile.network.repository.caregiver

import com.athelohealth.mobile.extensions.parseResponseWithoutBody
import com.athelohealth.mobile.network.api.CaregiverApi
import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.caregiver.CaregiverRelationshipDto
import com.athelohealth.mobile.network.dto.caregiver.InvitationDto
import com.athelohealth.mobile.network.repository.BaseRepository
import com.athelohealth.mobile.utils.UserManager

class CaregiverRepositoryImpl constructor(userManager: UserManager) :
    BaseRepository<CaregiverApi>(CaregiverApi::class.java, userManager), CaregiverRepository {
    override suspend fun loadCaregivers(nextUrl: String?): PageResponseDto<CaregiverRelationshipDto> {
        return if (nextUrl.isNullOrBlank()) service.getCaregivers() else service.getCaregivers(
            nextUrl
        )
    }

    override suspend fun loadPendingInvitation(nextUrl: String?): PageResponseDto<InvitationDto> {
        return if (nextUrl.isNullOrBlank()) service.getPendingInvitations()
        else service.getPendingInvitationsNext(nextUrl)
    }

    override suspend fun cancelPendingInvitation(id: Int): Boolean {
        return service.postCancelPendingInvitations(id).parseResponseWithoutBody()
    }

    override suspend fun removeCaregiver(id: Int): Boolean {
        return service.deleteCaregiver(id).parseResponseWithoutBody()
    }

    override suspend fun loadPatients(nextUrl: String?): PageResponseDto<CaregiverRelationshipDto> {
        return if (nextUrl.isNullOrBlank()) service.getPatients() else service.getPatients(nextUrl)
    }

    override suspend fun removePatient(id: String): Boolean {
        return service.deletePatient(id).parseResponseWithoutBody()
    }

    override suspend fun sendInvitation(
        nickName: String,
        email: String,
        relation: String
    ): Boolean {
        return service.postCaregiverInvite(
            body = mapOf(
                "receiver_nick_name" to nickName,
                "email" to email,
                "relation_label" to relation
            )
        ).parseResponseWithoutBody()
    }

    override suspend fun confirmInvitation(code: String): Boolean {
        return service.postCaregiverInviteConsume(
            code = code,
            body = mapOf("code" to code)
        ).parseResponseWithoutBody()
    }
}