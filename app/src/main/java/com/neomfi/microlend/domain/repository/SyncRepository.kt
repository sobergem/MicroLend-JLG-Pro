package com.neomfi.microlend.domain.repository

interface SyncRepository {
    suspend fun performBulkSync(): Boolean
}