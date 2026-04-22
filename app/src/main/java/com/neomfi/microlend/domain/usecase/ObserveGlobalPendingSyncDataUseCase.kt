package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.domain.repository.JlgGroupRepository
import com.neomfi.microlend.domain.repository.LeadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveGlobalPendingSyncDataUseCase @Inject constructor(
    private val groupRepository: JlgGroupRepository,
    private val leadRepository: LeadRepository
) {
    operator fun invoke(): Flow<Boolean>{
        return combine(
            leadRepository.hasUnSyncedLead(),
            groupRepository.hasUnSyncedGroup()
        ){pendingLeads, pendingGroups ->
            pendingLeads|| pendingGroups
        }
    }
}