package com.athelohealth.mobile.network.dto.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UpdateUserPhotoBodyDto(@SerialName("photo_id") private val photoId: Int)