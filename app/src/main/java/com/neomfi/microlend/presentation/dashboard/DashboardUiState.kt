package com.neomfi.microlend.presentation.dashboard

import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.model.VillageCenter

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data object Empty: DashboardUiState
    data class Success(
        val centers: List<VillageCenter>,
        val selectedCenterId: String,
        val groups:List<JlgGroupEntity>,
        val unassignedLeads: List<Lead>,
        val hasUnSyncedData: Boolean,
        val displaySyncState: androidx.work.WorkInfo.State?
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}