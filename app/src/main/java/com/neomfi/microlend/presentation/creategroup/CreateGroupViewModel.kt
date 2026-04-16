package com.neomfi.microlend.presentation.creategroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import com.neomfi.microlend.domain.model.Lead
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
    private val getUnassignedLeadsUseCase: GetUnassignedLeadsUseCase,
    private val insertGroupUseCase: InsertGroupUseCase,
    private val updateLeadUseCase: UpdateLeadUseCase
): ViewModel() {
    private val currentCenterID = "CENTER_123"

    val unassignedLeads: StateFlow<List<Lead>> = getUnassignedLeadsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun createGroup(groupName: String, selectedLeads : List<Lead>, onSuccess:() -> Unit){
        viewModelScope.launch{
            val newGroupId = UUID.randomUUID().toString()

            val newGroup = JlgGroupEntity(
                id = newGroupId,
                centerID = currentCenterID,
                name = groupName,
                syncStatus = SyncStatus.PENDING
            )

            insertGroupUseCase(newGroup)

            selectedLeads.forEach { lead ->
                val updatedLead = lead.copy(groupId = newGroupId)
                updateLeadUseCase(updatedLead)
            }

            onSuccess()
        }
    }

}