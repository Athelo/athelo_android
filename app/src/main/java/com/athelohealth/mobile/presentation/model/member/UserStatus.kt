package com.athelohealth.mobile.presentation.model.member

enum class UserStatus(val value: Int, val label: String) {
    FRIENDS(1, "Friend"),
    INVITATION_RECEIVED(2, "Invitation Received"),
    WE_REJECTED_THEIR_INVITE(3, "We Rejected Their Invite"),
    INVITATION_SENT(4, "Invitation Sent"),
    NO_CONNECTED(5, "Not Connected")
}

fun Int?.toUserStatus(): UserStatus? = UserStatus.values().firstOrNull { it.value == this }
