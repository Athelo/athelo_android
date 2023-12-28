package com.athelohealth.mobile.presentation.model.caregiver

import com.athelohealth.mobile.presentation.model.base.Image

data class Caregiver(val photo: Image?, val displayName: String, val id: Int, val relation: String, val relationId: String)
