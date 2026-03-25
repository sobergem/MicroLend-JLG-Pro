package com.neomfi.microlend.domain.repository

import com.neomfi.microlend.data.local.entity.VillageCenterEntity
import kotlinx.coroutines.flow.Flow

interface VillageCenterRepository {
    fun getAllCenters(): Flow<List<VillageCenterEntity>>
    suspend fun insertCenter(center: VillageCenterEntity)
}
