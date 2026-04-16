package com.neomfi.microlend.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.neomfi.microlend.data.MicroLendDatabase
import com.neomfi.microlend.data.dao.JlgGroupDao
import com.neomfi.microlend.data.dao.LeadDao
import com.neomfi.microlend.data.remote.api.MicroLendApi
import com.neomfi.microlend.data.remote.dto.BulkSyncRequest
import com.neomfi.microlend.data.remote.mapper.toDbString
import com.neomfi.microlend.data.remote.mapper.toDomain
import com.neomfi.microlend.data.remote.mapper.toDto
import com.neomfi.microlend.domain.model.SyncStatus
import com.neomfi.microlend.domain.repository.SyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val leadDao : LeadDao,
    private val groupDao: JlgGroupDao,
    private val api: MicroLendApi,
    private val database: MicroLendDatabase
): SyncRepository {
    override suspend fun performBulkSync(): Boolean {
        if(!syncMutex.tryLock()){
            return true
        }
        return try{
            _isSyncing.value = true
            val pendingSyncStatus = SyncStatus.PENDING.toDbString()
            val syncedStatus = SyncStatus.SYNCED.toDbString()
            val unSyncedLeads = leadDao.getLeadsBySyncStatus(pendingSyncStatus)
            val unSyncedGroups = groupDao.getGroupsBySyncStatus(pendingSyncStatus)

            if(unSyncedGroups.isEmpty() && unSyncedLeads.isEmpty()){
                return true
        }
            val request = BulkSyncRequest(unSyncedGroups.map{it.toDto()}, unSyncedLeads.map{it.toDto()})
            val response = api.syncBulkData(request)
            if(response.isSuccessful){
                val groupIds = unSyncedGroups.map { it.id }
                val leadIds = unSyncedLeads.map { it.id }

                database.withTransaction {
                    if (groupIds.isNotEmpty()) groupDao.markGroupsAsSynced(groupIds, syncedStatus)
                    if (leadIds.isNotEmpty()) leadDao.markLeadsAsSynced(leadIds, syncedStatus)
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
        }finally{
            _isSyncing.value = false
            syncMutex.unlock()
        }
    }

    private val syncMutex = Mutex()
    private val _isSyncing = MutableStateFlow(false)
    override val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()
}