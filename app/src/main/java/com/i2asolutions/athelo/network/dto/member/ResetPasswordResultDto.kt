package com.i2asolutions.athelo.network.dto.member

import com.i2asolutions.athelo.presentation.model.member.ResetPasswordResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ResetPasswordResultDto(@SerialName("detail") val detail: String) {
    fun toResetPasswordResult() = ResetPasswordResult(detail)
}