package com.athelohealth.mobile.network.dto.enums

import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.utils.parse.WsEnumSerializer
import kotlinx.serialization.Serializable

@Serializable(WsEnumSerializer::class)
class EnumItemDto(val id: String, val label: String) {

    fun toEnumItem(): EnumItem = EnumItem(id, label)
}