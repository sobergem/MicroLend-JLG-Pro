package com.neomfi.microlend.data.converter

import androidx.room.TypeConverter
import com.neomfi.microlend.data.local.entity.SyncStatus

class SyncStatusConverter {
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name

    @TypeConverter
    fun toSyncStatus(status: String): SyncStatus = SyncStatus.valueOf(status)
}