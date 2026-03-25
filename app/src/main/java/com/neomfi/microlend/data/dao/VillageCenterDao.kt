package com.neomfi.microlend.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neomfi.microlend.data.local.entity.VillageCenterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VillageCenterDao {
    @Query("SELECT * FROM village_centers")
    fun getAllCenters(): Flow<List<VillageCenterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCenter(centerDao: VillageCenterEntity)
}