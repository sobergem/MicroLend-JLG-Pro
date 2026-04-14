package com.neomfi.microlend.presentation.dashboard

import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(
        val groups:List<JlgGroupEntity>,
        val unassignedLeads: List<LeadEntity>,
        val hasUnSyncedData: Boolean
    ) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}