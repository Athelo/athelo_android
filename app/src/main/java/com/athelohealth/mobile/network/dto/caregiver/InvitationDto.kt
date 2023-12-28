package com.athelohealth.mobile.network.dto.caregiver

import com.athelohealth.mobile.extensions.toDate
import com.athelohealth.mobile.presentation.model.caregiver.Invitation
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class InvitationDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("relation_label") val relationLabel: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("expires_at") val expiresAt: String? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("receiver_nick_name") val nickname: String? = null,
) {
    fun toInvitation(relations: List<EnumItem>): Invitation {
        val relation = relations.first { it.id == relationLabel }
        val now = Date()
        val expire = expiresAt.toDate()
        if (now >= expire) return Invitation.ExpireInvitation(
            id = id ?: 0,
            name = nickname ?: "",
            email = email ?: "",
            relation = relation,
        ) else
            return when (status) {
                "consumed" ->
                    Invitation.ConsumedInvitation(
                        id = id ?: 0,
                        name = nickname ?: "",
                        email = email ?: "",
                        relation = relation,
                    )
                "canceled" ->
                    Invitation.CanceledInvitation(
                        id = id ?: 0,
                        name = nickname ?: "",
                        email = email ?: "",
                        relation = relation,
                    )
                "sent" ->
                    Invitation.PendingInvitation(
                        id = id ?: 0,
                        name = nickname ?: "",
                        email = email ?: "",
                        relation = relation,
                    )
                else -> Invitation.CanceledInvitation(
                    id = id ?: 0,
                    name = nickname ?: "",
                    email = email ?: "",
                    relation = relation,
                )
            }
    }
}