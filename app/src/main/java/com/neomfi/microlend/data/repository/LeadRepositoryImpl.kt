package com.neomfi.microlend.data.repository

import com.neomfi.microlend.data.dao.LeadDao
import com.neomfi.microlend.data.remote.mapper.toDomain
import com.neomfi.microlend.data.remote.mapper.toEntity
import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.repository.LeadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LeadRepositoryImpl @Inject constructor(
    private val dao: LeadDao
) : LeadRepository{
    override fun getAllLeads(): Flow<List<Lead>> = dao.getAllLeads().map{entityList ->
        entityList.map{it.toDomain()}
    }
    override fun getUnassignedLeads(centerId: String): Flow<List<Lead>> = dao.getUnassignedLeads(centerId).map{entityList ->
        entityList.map{it.toDomain()}
    }

    override fun getLeadsByGroup(groupId: String): Flow<List<Lead>> = dao.getLeadsByGroupId(groupId).map{entityList ->
        entityList.map{it.toDomain()}
    }

    override suspend fun insertLead(lead: Lead) = dao.insertLead(lead.toEntity())

    override suspend fun updateLead(lead: Lead) = dao.updateLead(lead.toEntity())

    override suspend fun deleteLead(lead: Lead) = dao.deleteLead(lead.toEntity())
    override suspend fun updateSyncStatus(
        leadIds: List<String>,
        newStatus: String
    ) {
        dao.markLeadsAsSynced(leadIds, newStatus)
    }

    override fun hasUnSyncedLead(): Flow<Boolean> = dao.hasUnSyncedLeads()

}