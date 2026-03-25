package com.neomfi.microlend.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

enum class SyncStatus{
    UNASSIGNED, ASSIGNED, QUEUED, SYNCING, SYNCED, FAILED
}
@Entity(tableName = "village_centers")
data class VillageCenterEntity(
    @PrimaryKey val id:String,
    val name: String,
    val pinCode: String
)

@Entity(tableName = "jlg_groups")
data class JlgGroupEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val centerID: String,
    val name: String,
    val syncStatus : SyncStatus
)

data class GroupWithMembers(
    @Embedded
    val group: JlgGroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupID"
    )
    val members: List<LeadEntity>
)

@Entity(tableName = "leads")
data class LeadEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val centerID: String,
    val groupID: String?,
    val name: String,
    val aadhaarNumber: String,
    val syncStatus: SyncStatus
)