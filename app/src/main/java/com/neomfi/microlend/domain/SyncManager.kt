package com.neomfi.microlend.domain

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    fun enqueueManualSync()
    fun observeSyncState(): Flow<androidx.work.WorkInfo.State?>
}