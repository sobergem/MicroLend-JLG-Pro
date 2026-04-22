package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.repository.LeadRepository
import javax.inject.Inject

class UpdateLeadUseCase @Inject constructor(
    private val repository : LeadRepository
) {
    suspend operator fun invoke(lead : Lead){
        repository.updateLead(lead)
    }
}