package com.i2asolutions.athelo.useCase.caregiver

import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.model.enums.Enums
import javax.inject.Inject

class LoadRelationsUseCase @Inject constructor(private val enums: Enums) {

    suspend operator fun invoke(): List<EnumItem> {
        return enums.caregiverRelations
    }
}