package com.athelohealth.mobile.useCase.caregiver

import com.athelohealth.mobile.network.repository.caregiver.CaregiverRepository
import com.athelohealth.mobile.presentation.model.base.PageResponse
import com.athelohealth.mobile.presentation.model.caregiver.Caregiver
import com.athelohealth.mobile.presentation.model.enums.Enums
import javax.inject.Inject

class LoadCaregiversUseCase @Inject constructor(
    private val repository: CaregiverRepository,
    private val enums: Enums
) {

    suspend operator fun invoke(nextUrl: String?): PageResponse<Caregiver> {
        return repository.loadCaregivers(nextUrl).toPageResponse { relation ->
            relation.caregiver?.toCaregiver(
                enums.caregiverRelations.firstOrNull { enum -> enum.id == relation.relations }?.label
                    ?: "", relation.id?.toString() ?: ""
            )
        }
    }
}