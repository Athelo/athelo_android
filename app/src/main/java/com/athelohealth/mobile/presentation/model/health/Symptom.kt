package com.athelohealth.mobile.presentation.model.health

import android.os.Parcelable
import com.athelohealth.mobile.presentation.model.base.Image
import com.athelohealth.mobile.presentation.model.enums.EnumItem
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
