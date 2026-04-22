package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.domain.model.JlgGroup
import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.model.SyncStatus
import com.neomfi.microlend.domain.repository.JlgGroupRepository
import java.util.UUID
import javax.inject.Inject

class CreateGroupWithLeadsUseCase @Inject constructor(
    private val jlgGroupRepository: JlgGroupRepository,
){
    suspend operator fun invoke(centerId: String, groupName: String, selectedLeads: List<Lead>){
        val newGroup = JlgGroup(
            id = UUID.randomUUID().toString(),
            centerID = centerId,
            name = groupName,
            syncStatus = SyncStatus.PENDING
        )
        val leadIds = selectedLeads.map { it.id }

        jlgGroupRepository.createGroupWithLeads(newGroup, leadIds)
    }
}