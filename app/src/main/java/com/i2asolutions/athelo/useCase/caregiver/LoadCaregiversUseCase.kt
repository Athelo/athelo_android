package com.i2asolutions.athelo.useCase.caregiver

import com.i2asolutions.athelo.network.repository.caregiver.CaregiverRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.caregiver.Caregiver
import com.i2asolutions.athelo.presentation.model.enums.Enums
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