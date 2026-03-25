package com.neomfi.microlend.data.repository

import com.neomfi.microlend.data.dao.LeadDao
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.domain.repository.LeadRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LeadRepositoryImpl @Inject constructor(
    private val dao: LeadDao
) : LeadRepository{
    override fun getAllLeads(): Flow<List<LeadEntity>> = dao.getAllLeads()

    override fun getUnassignedLeads(): Flow<List<LeadEntity>> = dao.getUnassignedLeads()

    override fun getLeadsByGroup(groupId: String): Flow<List<LeadEntity>> = dao.getLeadsByGroupId(groupId)

    override suspend fun insertLead(lead: LeadEntity) = dao.insertLead(lead)

    override suspend fun updateLead(lead: LeadEntity) = dao.updateLead(lead)

    override suspend fun deleteLead(lead: LeadEntity) = dao.deleteLead(lead)

}