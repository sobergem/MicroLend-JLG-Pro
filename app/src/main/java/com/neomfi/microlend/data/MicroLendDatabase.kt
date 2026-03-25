package com.neomfi.microlend.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.neomfi.microlend.data.converter.SyncStatusConverter
import com.neomfi.microlend.data.dao.JlgGroupDao
import com.neomfi.microlend.data.dao.LeadDao
import com.neomfi.microlend.data.dao.VillageCenterDao
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.VillageCenterEntity


@Database(
    entities = [
        VillageCenterEntity::class,
        JlgGroupEntity::class,
        LeadEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(SyncStatusConverter::class)
abstract class MicroLendDatabase: RoomDatabase() {
    abstract fun leadDao(): LeadDao
    abstract fun groupDao(): JlgGroupDao
    abstract fun centerDao(): VillageCenterDao
}