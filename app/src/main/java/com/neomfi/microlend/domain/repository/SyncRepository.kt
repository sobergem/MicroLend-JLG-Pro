package com.neomfi.microlend.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface SyncRepository {
    val isSyncing: StateFlow<Boolean>
    suspend fun performBulkSync(): Boolean
}