package com.neomfi.microlend.domain.repository

import com.neomfi.microlend.data.local.entity.LeadEntity
import kotlinx.coroutines.flow.Flow

interface LeadRepository {
    fun getAllLeads(): Flow<List<LeadEntity>>
    fun getUnassignedLeads(): Flow<List<LeadEntity>>
    fun getLeadsByGroup(groupId: String): Flow<List<LeadEntity>>

    suspend fun insertLead(lead: LeadEntity)
    suspend fun updateLead(lead: LeadEntity)
    suspend fun deleteLead(lead: LeadEntity)
}