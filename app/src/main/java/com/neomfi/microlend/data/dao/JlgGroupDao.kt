package com.neomfi.microlend.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JlgGroupDao {
    @Query("SELECT * FROM jlg_groups WHERE centerID = :centerId ORDER BY name ASC")
    fun getGroupsByCenter(centerId: String): Flow<List<JlgGroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: JlgGroupEntity)

    @Delete
    suspend fun deleteGroup(group: JlgGroupEntity)

    @Query("SELECT * FROM jlg_groups WHERE syncStatus = :syncStatus")
    suspend fun getGroupsBySyncStatus(syncStatus: String): List<JlgGroupEntity>
    @Query("UPDATE jlg_groups SET syncStatus = :syncStatus WHERE id IN (:groupIds)")
    suspend fun markGroupsAsSynced(groupIds: List<String>, syncStatus: String)
}