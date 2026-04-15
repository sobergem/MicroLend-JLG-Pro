package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.domain.repository.VillageCenterRepository
import javax.inject.Inject

class SyncCenterUseCase @Inject constructor(
    private val repository: VillageCenterRepository
){
    suspend operator fun invoke(): Result<Unit> = repository.fetchAndCacheCenters()
}