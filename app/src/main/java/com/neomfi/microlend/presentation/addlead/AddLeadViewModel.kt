package com.neomfi.microlend.presentation.addlead

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import com.neomfi.microlend.domain.usecase.InsertLeadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLeadViewModel @Inject constructor(
   private val insertLeadUseCase: InsertLeadUseCase
) : ViewModel(){
    private val currentCenterId = "CENTER_123"

    fun saveLead(name: String, aadhaarNumber: String, onSuccess:() -> Unit){
        viewModelScope.launch {
            val newLead = LeadEntity(
                centerID = currentCenterId,
                groupID = null,
                name = name,
                aadhaarNumber = aadhaarNumber,
                syncStatus = SyncStatus.UNASSIGNED
            )
            insertLeadUseCase(newLead)
            onSuccess()
        }
    }
}