package com.neomfi.microlend.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.neomfi.microlend.data.local.entity.LeadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LeadDao {
    //1. Get all leads for the main dashboard
    @Query("SELECT * FROM leads ORDER BY name ASC")
    fun getAllLeads(): Flow<List<LeadEntity>>

    //2. Get only leads that belong to a specific JLG group
    @Query("SELECT * FROM leads WHERE groupID= :groupId")
    fun getLeadsByGroupId(groupId: String): Flow<List<LeadEntity>>

    //3. Get leads that haven't been assigned to any group yet
    @Query("SELECT * FROM leads WHERE groupID IS NULL AND centerId = :centerId")
    fun getUnassignedLeads(centerId: String):Flow<List<LeadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: LeadEntity)

    @Update
    suspend fun updateLead(lead: LeadEntity)

    @Delete
    suspend fun deleteLead(lead: LeadEntity)

    @Query("SELECT * FROM leads WHERE syncStatus = :syncStatus")
    suspend fun getLeadsBySyncStatus(syncStatus: String): List<LeadEntity>
    @Query("UPDATE leads SET syncStatus = :syncStatus WHERE id IN (:leadIds)")
    suspend fun markLeadsAsSynced(leadIds: List<String>, syncStatus: String)

    @Query("SELECT EXISTS(SELECT 1 FROM leads WHERE syncStatus = 'PENDING')")
    fun hasUnSyncedLeads() : Flow<Boolean>
}