package com.neomfi.microlend.domain.repository

import com.neomfi.microlend.domain.model.VillageCenter
import kotlinx.coroutines.flow.Flow

interface VillageCenterRepository {
    fun getAllCenters(): Flow<List<VillageCenter>>

    suspend fun fetchAndCacheCenters(): Result<Unit>
}
