package com.athelohealth.mobile.useCase.caregiver

import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.model.enums.Enums
import javax.inject.Inject

class LoadRelationsUseCase @Inject constructor(private val enums: Enums) {

    suspend operator fun invoke(): List<EnumItem> {
        return enums.caregiverRelations
    }
}