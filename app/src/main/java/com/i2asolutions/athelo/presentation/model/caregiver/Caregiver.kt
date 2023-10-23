package com.i2asolutions.athelo.presentation.model.caregiver

import com.i2asolutions.athelo.presentation.model.base.Image

data class Caregiver(val photo: Image?, val displayName: String, val id: Int, val relation: String, val relationId: String)
