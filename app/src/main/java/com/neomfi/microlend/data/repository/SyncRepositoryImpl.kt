package com.neomfi.microlend.data.repository

import android.util.Log
import com.neomfi.microlend.data.dao.JlgGroupDao
import com.neomfi.microlend.data.dao.LeadDao
import com.neomfi.microlend.data.local.entity.SyncStatus
import com.neomfi.microlend.data.remote.api.MicroLendApi
import com.neomfi.microlend.data.remote.dto.BulkSyncRequest
import com.neomfi.microlend.data.remote.mapper.toDto
import com.neomfi.microlend.domain.repository.SyncRepository
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val leadDao : LeadDao,
    private val groupDao: JlgGroupDao,
    private val api: MicroLendApi
): SyncRepository {
    override suspend fun performBulkSync(): Boolean {
        return try{
            val unSyncedLeads = leadDao.getLeadsBySyncStatus(SyncStatus.UNASSIGNED.name)
            val unSyncedGroups = groupDao.getGroupsBySyncStatus(SyncStatus.UNASSIGNED.name)

            if(unSyncedGroups.isEmpty() && unSyncedLeads.isEmpty()){
                return true
        }
            val request = BulkSyncRequest(unSyncedGroups.map{it.toDto()}, unSyncedLeads.map{it.toDto()})
            val response = api.syncBulkData(request)
            if(response.isSuccessful){
                unSyncedGroups.forEach { group ->
                    groupDao.insertGroup(group.copy(syncStatus = SyncStatus.SYNCED))
                }

                // Update Leads
                unSyncedLeads.forEach { lead ->
                    leadDao.insertLead(lead.copy(syncStatus = SyncStatus.SYNCED))
                }

                Log.d("SyncRepository", "Bulk sync successful!")
                true
            }else{
                Log.e("SyncRepository", "Server rejected the sync: ${response.code()}")
                false
            }
        }catch (e: Exception){
            Log.e("SyncRepository", "Error during sync: ${e.message}")
            false
        }
    }
}