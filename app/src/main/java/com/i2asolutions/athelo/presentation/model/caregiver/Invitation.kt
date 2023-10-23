package com.i2asolutions.athelo.presentation.model.caregiver

import com.i2asolutions.athelo.presentation.model.enums.EnumItem

sealed interface Invitation {
    val id: Int
    val name: String
    val email: String

    data class ExpireInvitation(
        override val id: Int,
        override val name: String,
        override val email: String,
        val relation: EnumItem,
    ) : Invitation

    data class PendingInvitation(
        override val id: Int,
        override val name: String,
        override val email: String,
        val relation: EnumItem,
    ) : Invitation

    data class CanceledInvitation(
        override val id: Int,
        override val name: String,
        override val email: String,
        val relation: EnumItem,
    ) : Invitation

    data class ConsumedInvitation(
        override val id: Int,
        override val name: String,
        override val email: String,
        val relation: EnumItem,
    ) : Invitation
}