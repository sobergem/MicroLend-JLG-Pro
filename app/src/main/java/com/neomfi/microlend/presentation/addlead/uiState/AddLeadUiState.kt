package com.neomfi.microlend.presentation.addlead.uiState

import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.model.VillageCenter

data class AddLeadUiState(
    val name: String= "",
    val aadhaar: String = "",
    val phone: String = "",
    val monthlyIncome: String = "",
    val monthlyExpenses: String = "",
    val selectedCenterId: String ="",
    val availableCenters: List<VillageCenter> = emptyList(),

    val isLoading : Boolean = false,
    val errorMessage : String? = null
) {
    fun toLead() : Lead {
        return Lead(
            name = name,
            aadhaarNumber = aadhaar,
            phone = phone,
            monthlyIncome = monthlyIncome.toDouble(),
            monthlyExpenses = monthlyExpenses.toDouble(),
            centerId = selectedCenterId,
            groupId = null,
        )
    }
    val isFormValid: Boolean
        get() = name.isNotBlank() &&
                aadhaar.length == 12 && aadhaar.all{it.isDigit()} &&
                phone.length == 10 && phone.all{it.isDigit()} &&
                monthlyIncome.isNotBlank() &&
                monthlyExpenses.isNotBlank() &&
                selectedCenterId.isNotBlank()
}
