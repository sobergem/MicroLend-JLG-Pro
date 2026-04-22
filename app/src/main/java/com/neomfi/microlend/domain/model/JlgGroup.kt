package com.neomfi.microlend.domain.model

data class JlgGroup(
    val id: String,
    val centerID: String,
    val name: String,
    val syncStatus: SyncStatus
)
