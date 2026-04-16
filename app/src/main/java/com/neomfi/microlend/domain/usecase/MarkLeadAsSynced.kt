package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.domain.repository.LeadRepository
import javax.inject.Inject

class MarkLeadAsSynced @Inject constructor(
    private val repository: LeadRepository
) {
    suspend operator fun invoke(leadIds: List<String>, newStatus: String) = repository.updateSyncStatus(leadIds, newStatus)
}