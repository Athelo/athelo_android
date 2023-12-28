package com.athelohealth.mobile.network.dto.member

import com.athelohealth.mobile.presentation.model.member.ResetPasswordResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ResetPasswordResultDto(@SerialName("detail") val detail: String) {
    fun toResetPasswordResult() = ResetPasswordResult(detail)
}