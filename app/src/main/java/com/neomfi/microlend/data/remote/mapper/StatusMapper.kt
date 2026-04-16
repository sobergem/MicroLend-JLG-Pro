package com.neomfi.microlend.data.remote.mapper

import com.neomfi.microlend.domain.model.AssignmentStatus
import com.neomfi.microlend.domain.model.SyncStatus

fun SyncStatus.toDbString() : String = when(this){
    SyncStatus.PENDING -> "PENDING"
    SyncStatus.SYNCING -> "SYNCING"
    SyncStatus.SYNCED -> "SYNCED"
    SyncStatus.FAILED -> "FAILED"
}

fun String.toSyncStatus(): SyncStatus = when(this){
    "PENDING" -> SyncStatus.PENDING
    "SYNCING" -> SyncStatus.SYNCING
    "SYNCED" -> SyncStatus.SYNCED
    "FAILED" -> SyncStatus.FAILED
    else -> SyncStatus.PENDING
}

fun AssignmentStatus.toDbString(): String = when(this){
    AssignmentStatus.UNASSIGNED -> "UNASSIGNED"
    AssignmentStatus.ASSIGNED -> "ASSIGNED"
    AssignmentStatus.REJECTED -> "REJECTED"
}

fun String.toAssignmentStatus(): AssignmentStatus =when(this){
    "UNASSIGNED" -> AssignmentStatus.UNASSIGNED
    "ASSIGNED" -> AssignmentStatus.ASSIGNED
    "REJECTED" -> AssignmentStatus.REJECTED
    else -> AssignmentStatus.UNASSIGNED
}
