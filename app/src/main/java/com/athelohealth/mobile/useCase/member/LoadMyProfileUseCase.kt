package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.extensions.errorMessageOrUniversalMessage
import com.athelohealth.mobile.network.dto.member.PatientStatus
import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.UserManager
import retrofit2.HttpException
import javax.inject.Inject

class LoadMyProfileUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(): User? {
        val response = repository.runCatching { getMyProfile().results.firstOrNull() }
        return if (response.isSuccess) {
            response.getOrNull()?.toUser()
        } else {
            val exception = response.exceptionOrNull()
            if (exception is HttpException && exception.code() == 401) {
                throw AuthorizationException(throwable = exception)
            } else {
                throw AuthorizationException(
                    message = exception.errorMessageOrUniversalMessage,
                    throwable = exception
                )
            }
        }
    }

    suspend fun treatmentStatus(): PatientStatus {
        return repository.getPatientStatus()
    }

}