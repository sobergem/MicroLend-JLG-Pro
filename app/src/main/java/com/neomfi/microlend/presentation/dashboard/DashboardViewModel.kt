package com.neomfi.microlend.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neomfi.microlend.domain.usecase.GetGroupsByCenterUseCase
import com.neomfi.microlend.domain.usecase.GetUnassignedLeadsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getGroupsByCenter: GetGroupsByCenterUseCase,
    getUnassignedLeads: GetUnassignedLeadsUseCase
) : ViewModel() {
    private val currentCenterId = "CENTER_123"
    val uiState: StateFlow<DashboardUiState> = combine(
        getGroupsByCenter(currentCenterId),
        getUnassignedLeads()
    ) { groups, leads ->
        DashboardUiState.Success(
            groups = groups,
            unassignedLeads = leads
        ) as DashboardUiState
    }
        .catch { e ->
            emit(DashboardUiState.Error(e.message ?: "An unknown error occurred"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState.Loading
        )
}
