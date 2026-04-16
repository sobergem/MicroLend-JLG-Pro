package com.neomfi.microlend.domain.model

import java.util.UUID

data class Lead(
    val id: String = UUID.randomUUID().toString(),
    val centerId: String,
    val groupId: String?,
    val name: String,
    val aadhaarNumber: String,
    val phone: String,
    val monthlyIncome: Double,
    val monthlyExpenses: Double,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val assignmentStatus: AssignmentStatus = AssignmentStatus.UNASSIGNED
)
