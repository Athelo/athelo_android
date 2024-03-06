package com.athelohealth.mobile.network.dto.chat

import com.athelohealth.mobile.extensions.displayAsDifferentDateFormat
import com.athelohealth.mobile.extensions.toDate
import com.athelohealth.mobile.presentation.model.chat.ConversationInfo
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.utils.consts.DATE_FORMAT_CHAT
import com.athelohealth.mobile.utils.consts.DATE_FORMAT_FULL
import com.athelohealth.mobile.websocket.constant.GET_HISTORY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.Locale

@Serializable
data class ChatMessages(
	@SerialName("content")
	val content: String,
	@SerialName("author_id")
	val authorId: Int,
	@SerialName("id")
	val id: Int,
	@SerialName("thread_id")
	val threadId: Int,
	@SerialName("updated_at")
	val updateAt: String
) {
	fun toConversation() =
		ConversationInfo.ConversationMessage(
			type = GET_HISTORY,
			messageId = id.toString(),
			message = content,
			isSystemMessage = true,
			chatId = threadId.toString(),
			userId = authorId.toString(),
			date = updateAt.toDate(DATE_FORMAT_CHAT, Locale.getDefault()) ?: Date(),
			user = User(id = authorId)
		)
}