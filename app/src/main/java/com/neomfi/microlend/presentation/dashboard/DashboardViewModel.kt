package com.neomfi.microlend.presentation.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.neomfi.microlend.data.local.entity.SyncStatus
import com.neomfi.microlend.domain.usecase.GetGroupsByCenterUseCase
import com.neomfi.microlend.domain.usecase.GetUnassignedLeadsUseCase
import com.neomfi.microlend.worker.SyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getGroupsByCenter: GetGroupsByCenterUseCase,
    getUnassignedLeads: GetUnassignedLeadsUseCase,
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    private val currentCenterId = "CENTER_123"
    val uiState: StateFlow<DashboardUiState> = combine(
        getGroupsByCenter(currentCenterId),
        getUnassignedLeads()
    ) { groups, leads ->
        val needsSync = groups.any{it.syncStatus== SyncStatus.PENDING} || leads.any{it.syncStatus == SyncStatus.PENDING}

        DashboardUiState.Success(
            groups = groups,
            unassignedLeads = leads,
            needsSync
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

    val syncWorkState = WorkManager.getInstance(context)
        .getWorkInfosForUniqueWorkFlow("CloudSync")
        .map{workInfos ->
            workInfos.firstOrNull()?.state
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    fun triggerManualSync(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>().setConstraints(constraints).build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "CloudSync",
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
    }
}
