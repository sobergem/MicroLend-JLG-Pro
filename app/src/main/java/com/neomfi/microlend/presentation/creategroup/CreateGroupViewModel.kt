package com.neomfi.microlend.presentation.creategroup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neomfi.microlend.data.remote.mapper.toDbString
import com.neomfi.microlend.domain.model.JlgGroup
import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.model.SyncStatus
import com.neomfi.microlend.domain.usecase.CreateGroupWithLeadsUseCase
import com.neomfi.microlend.domain.usecase.GetUnassignedLeadsUseCase
import com.neomfi.microlend.domain.usecase.InsertGroupUseCase
import com.neomfi.microlend.domain.usecase.UpdateLeadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getUnassignedLeadsUseCase: GetUnassignedLeadsUseCase,
    private val createGroupWithLeadsUseCase: CreateGroupWithLeadsUseCase
): ViewModel() {
    private val currentCenterID : String = checkNotNull(savedStateHandle["centerId"])
    val unassignedLeads: StateFlow<List<Lead>> = getUnassignedLeadsUseCase(currentCenterID).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun createGroup(groupName: String, selectedLeads : List<Lead>, onSuccess:() -> Unit){
        viewModelScope.launch{
            createGroupWithLeadsUseCase(currentCenterID, groupName, selectedLeads)
            onSuccess()
        }
    }

}