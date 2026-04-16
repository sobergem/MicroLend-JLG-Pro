package com.neomfi.microlend.domain.repository

import com.neomfi.microlend.domain.model.Lead
import kotlinx.coroutines.flow.Flow

interface LeadRepository {
    fun getAllLeads(): Flow<List<Lead>>
    fun getUnassignedLeads(): Flow<List<Lead>>
    fun getLeadsByGroup(groupId: String): Flow<List<Lead>>

    suspend fun insertLead(lead: Lead)
    suspend fun updateLead(lead: Lead)
    suspend fun deleteLead(lead: Lead)

    suspend fun updateSyncStatus(leadIds: List<String>, newStatus: String)
}