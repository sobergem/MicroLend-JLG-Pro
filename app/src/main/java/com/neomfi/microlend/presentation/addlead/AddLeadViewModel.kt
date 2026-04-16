package com.neomfi.microlend.presentation.addlead

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neomfi.microlend.domain.usecase.GetAllCenters
import com.neomfi.microlend.domain.usecase.SaveAndSyncSingleLeadUseCase
import com.neomfi.microlend.presentation.addlead.uiState.AddLeadUiState
import com.neomfi.microlend.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLeadViewModel @Inject constructor(
    private val saveAndSyncSingleLeadUseCase: SaveAndSyncSingleLeadUseCase,
    private val getAllCenters: GetAllCenters
) : ViewModel(){
    init {
        viewModelScope.launch{
            getAllCenters().collect{ centers ->
                _uiState.update{ it.copy(availableCenters = centers)}
            }
        }
    }
    private val _uiState = MutableStateFlow(AddLeadUiState())
    val uiState : StateFlow<AddLeadUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onNameChanged(newName: String) {
        _uiState.update { currentState ->
            currentState.copy(name = newName)
        }
    }

    fun onSaveClicked(){
        viewModelScope.launch {
            _uiState.update{
                currentState -> currentState.copy(isLoading = true)
            }
            val currentState = uiState.value
            val lead = currentState.toLead()
            saveAndSyncSingleLeadUseCase(lead)
            _uiState.update{
                    currentState -> currentState.copy(isLoading = false)
            }
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }

    fun onAadhaarChanged(newAadhaar: String) {
        _uiState.update{
            currentState -> currentState.copy(aadhaar = newAadhaar)
        }
    }

    fun onPhoneChanged(newPhone: String){
        _uiState.update{
            currentState -> currentState.copy(phone = newPhone)
        }
    }

    fun onMonthlyIncomeChanged(newMonthlyIncome: String){
        _uiState.update{
            currentState -> currentState.copy(monthlyIncome = newMonthlyIncome)
        }
    }

    fun onMonthlyExpensesChanged(newMonthlyExpenses: String){
        _uiState.update{
            currentState ->currentState.copy(monthlyExpenses = newMonthlyExpenses)
        }
    }

    fun onCenterSelected(newCenterId: String){
        _uiState.update{it.copy(selectedCenterId = newCenterId)}
    }
}