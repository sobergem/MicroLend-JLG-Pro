package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.domain.repository.LeadRepository
import javax.inject.Inject

class UpdateLeadUseCase @Inject constructor(
    private val repository : LeadRepository
) {
    suspend operator fun invoke(lead : LeadEntity){
        repository.updateLead(lead)
    }
}