package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.repository.LeadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUnassignedLeadsUseCase @Inject constructor(
    private val repository: LeadRepository
) {
    operator fun invoke(centerId: String): Flow<List<Lead>>{
        return repository.getUnassignedLeads(centerId)
    }
}