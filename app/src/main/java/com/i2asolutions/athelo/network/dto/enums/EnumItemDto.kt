package com.i2asolutions.athelo.network.dto.enums

import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.utils.parse.WsEnumSerializer
import kotlinx.serialization.Serializable

@Serializable(WsEnumSerializer::class)
class EnumItemDto(val id: String, val label: String) {

    fun toEnumItem(): EnumItem = EnumItem(id, label)
}