package com.neomfi.microlend.presentation.dashboard

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.neomfi.microlend.domain.SyncManager
import com.neomfi.microlend.domain.model.JlgGroup
import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.model.VillageCenter
import com.neomfi.microlend.domain.usecase.GetAllCenters
import com.neomfi.microlend.domain.usecase.GetGroupsByCenterUseCase
import com.neomfi.microlend.domain.usecase.GetUnassignedLeadsUseCase
import com.neomfi.microlend.domain.usecase.ObserveGlobalPendingSyncDataUseCase
import com.neomfi.microlend.domain.usecase.SyncCenterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getGroupsByCenter: GetGroupsByCenterUseCase,
    getUnassignedLeads: GetUnassignedLeadsUseCase,
    getAllCenters: GetAllCenters,
    syncCenterUseCase: SyncCenterUseCase,
    private val syncManager: SyncManager,
    observeGlobalPendingSyncDataUseCase: ObserveGlobalPendingSyncDataUseCase
) : ViewModel() {
    private val _selectedCenterId = MutableStateFlow<String?>(null)
    private val _initError = MutableStateFlow<String?>(null)

    init{
        viewModelScope.launch {
           val result = syncCenterUseCase()
            result.onFailure { error ->
                _initError.value = error.message
            }
            result.onSuccess {
                val centers = getAllCenters().first()
                _selectedCenterId.value = if (centers.isNotEmpty()) centers.first().id else "EMPTY_STATE"
            }
        }
    }
    fun onCenterSelected(centerId: String){
        _selectedCenterId.value = centerId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DashboardUiState> = combine(
        getAllCenters(),
        _selectedCenterId,
        _initError
    )
    {
        centers, currentCenterId, error -> Triple(centers, currentCenterId, error)
    }.flatMapLatest { (centers, currentCenterId, error) ->
        when{
            error != null && centers.isEmpty() ->{
                flowOf(DashboardUiState.Error(error))
            }
            centers.isEmpty() && currentCenterId == "EMPTY_STATE" ->{
                flowOf(DashboardUiState.Empty)

            }
            currentCenterId == null ->{
                flowOf(DashboardUiState.Loading)
            }
            else ->{
                combine(
                    getGroupsByCenter(centerId =  currentCenterId),
                    getUnassignedLeads(centerId = currentCenterId),
                    observeGlobalPendingSyncDataUseCase(),
                    syncManager.observeSyncState()
                ) { groups, leads, hasGlobalPendingSync, workState ->
                    buildSuccessState(centers, currentCenterId, groups, leads, workState, hasGlobalPendingSync)
                }
            }
        }
    }.catch { e->
        emit(DashboardUiState.Error(e.message?:"Unkonwn Error"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState.Loading
    )

    private fun buildSuccessState(
        centers: List<VillageCenter>,
        currentCenterId: String,
        groups: List<JlgGroup>,
        leads: List<Lead>,
        workState: WorkInfo.State?,
        hasGlobalPendingSync: Boolean
    ): DashboardUiState {
        val calculateState = if (hasGlobalPendingSync && workState == WorkInfo.State.SUCCEEDED){
           null
        }else{
            workState
        }
        return DashboardUiState.Success(
            centers,
            currentCenterId,
            groups,
            leads,
            hasGlobalPendingSync,
            calculateState
        )
    }


    fun triggerManualSync(){
        syncManager.enqueueManualSync()
    }
}
