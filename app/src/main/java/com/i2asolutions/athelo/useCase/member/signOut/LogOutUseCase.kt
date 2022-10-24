package com.i2asolutions.athelo.useCase.member.signOut

import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val clearUserInformationUseCase: ClearUserInformationUseCase,
    private val clearPersonalInformationUseCase: ClearPersonalInformationUseCase,
    private val clearConnectionStateUseCase: ClearConnectionStateUseCase,
) {

    suspend operator fun invoke() {
        clearUserInformationUseCase()
        clearPersonalInformationUseCase()
        clearConnectionStateUseCase()
    }
}