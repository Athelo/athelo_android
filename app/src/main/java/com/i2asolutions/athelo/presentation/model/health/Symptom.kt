package com.i2asolutions.athelo.presentation.model.health

import android.os.Parcelable
import com.i2asolutions.athelo.presentation.model.base.Image
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Symptom(
    val id: Int,
    val name: String,
    val comment: String? = null,
    val description: String? = null,
    val icon: Image? = null,
    val symptomId: Int
) : Parcelable {
    fun toEnumItem(): EnumItem {
        return EnumItem(id.toString(), name)
    }
}
