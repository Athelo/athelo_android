package com.athelohealth.mobile.presentation.model.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EnumItem(val id: String, val label: String) : Parcelable {
    override fun toString(): String {
        return label
    }

    companion object {
        val EMPTY = EnumItem("-1", "Choose Item")
    }
}