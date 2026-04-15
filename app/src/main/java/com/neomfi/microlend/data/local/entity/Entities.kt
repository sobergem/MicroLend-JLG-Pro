package com.neomfi.microlend.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

enum class SyncStatus {
    PENDING,  // Waiting for WorkManager to pick it up
    SYNCING,  // Currently in transit
    SYNCED,   // Safely in the Node.js database
    FAILED    // Network crashed, needs retry
}

enum class AssignmentStatus {
    UNASSIGNED, // Sitting in the Field Officer's queue
    ASSIGNED,   // Attached to a JLG Group
    REJECTED    // KYC failed or manager declined
}

@Entity(tableName = "village_centers")
data class VillageCenterEntity(
    @PrimaryKey val id:String,
    val name: String,
    val villageName: String,
    val meetingDay: String,
    val pinCode: String? = null
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
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val assignmentStatus: AssignmentStatus = AssignmentStatus.UNASSIGNED
)