package com.athelohealth.mobile.network.dto.member

import com.athelohealth.mobile.presentation.model.enums.EnumItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CancerStatus {
    @SerialName("REMISSION")
    REMISSION,

    @SerialName("ACTIVE")
    ACTIVE
}

fun getCancerStatusList(): List<EnumItem> {
    return listOf(
        EnumItem(id = CancerStatus.REMISSION.name, label = CancerStatus.REMISSION.name),
        EnumItem(id = CancerStatus.ACTIVE.name, label = CancerStatus.ACTIVE.name)
    )
}

fun CancerStatus.getCurrentCancerStatus(): EnumItem {
    return EnumItem(id = name, label = name)
}