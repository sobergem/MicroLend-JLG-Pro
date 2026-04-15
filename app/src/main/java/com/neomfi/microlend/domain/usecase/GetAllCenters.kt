package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.domain.repository.VillageCenterRepository
import javax.inject.Inject

class GetAllCenters @Inject constructor(
    private val repository: VillageCenterRepository
){
    operator fun invoke() = repository.getAllCenters()
}