package com.neomfi.microlend.presentation.dashboard

import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.domain.model.Lead

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(
        val groups:List<JlgGroupEntity>,
        val unassignedLeads: List<Lead>,
        val hasUnSyncedData: Boolean,
        val displaySyncState: androidx.work.WorkInfo.State?
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}