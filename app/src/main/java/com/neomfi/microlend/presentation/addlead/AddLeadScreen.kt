package com.neomfi.microlend.presentation.addlead

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neomfi.microlend.presentation.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLeadScreen(
    onNavigationBack: () -> Unit,
    viewModel : AddLeadViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }
    val selectedCenterName = uiState.availableCenters.find { it.id == uiState.selectedCenterId }?.name?:
    "Select a Center"



    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect{ event ->
            when(event){
                is UiEvent.NavigateUp ->{
                    onNavigationBack()
                }
                is UiEvent.ShowSnackBar ->{

                }
                is UiEvent.Navigate ->{

                }
                is UiEvent.ShowToast ->{

                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Create New Lead")},
                navigationIcon = {
                    IconButton(onClick= onNavigationBack){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ){ paddingValues ->
       Column(
           modifier = Modifier.fillMaxSize()
               .padding(paddingValues)
               .padding(16.dp),
           verticalArrangement = Arrangement.spacedBy(16.dp)
       ){
           //Name Field
           OutlinedTextField(
               value = uiState.name,
               onValueChange = viewModel::onNameChanged,
               label = {Text("Full Name")},
               modifier = Modifier.fillMaxWidth(),
               singleLine = true
           )

           //Aadhaar Field
           OutlinedTextField(
               value = uiState.aadhaar,
               onValueChange = viewModel::onAadhaarChanged,
               label = { Text("Aadhaar Number (12 Digits)") },
               modifier = Modifier.fillMaxWidth(),
               singleLine = true,
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
           )
           OutlinedTextField(
               value = uiState.phone,
               onValueChange = viewModel::onPhoneChanged,
               label = { Text("Phone Number") },
               modifier = Modifier.fillMaxWidth(),
               singleLine = true,
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
           )

           OutlinedTextField(
               value = uiState.monthlyIncome,
               onValueChange = viewModel::onMonthlyIncomeChanged,
               label = { Text("Monthly Income") },
               modifier = Modifier.fillMaxWidth(),
               singleLine = true,
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
           )

           OutlinedTextField(
               value = uiState.monthlyExpenses,
               onValueChange = viewModel::onMonthlyExpensesChanged,
               label = { Text("Monthly Expenses") },
               modifier = Modifier.fillMaxWidth(),
               singleLine = true,
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
           )

           ExposedDropdownMenuBox(
               expanded = expanded,
               onExpandedChange = { expanded = !expanded },
               modifier = Modifier.fillMaxWidth()
           ) {
               OutlinedTextField(
                   value = selectedCenterName,
                   onValueChange = {},
                   readOnly = true,
                   label = { Text("Assign to Center") },
                   trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                   modifier = Modifier
                       .fillMaxWidth().menuAnchor(),
                   colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
               )
               ExposedDropdownMenu(
                   expanded = expanded,
                   onDismissRequest = { expanded = false }
               ){
                   if(uiState.availableCenters.isEmpty()){
                       DropdownMenuItem(
                           text = { Text("No centers downloaded yet") },
                           onClick = { expanded = false }
                       )
                   }else{
                       uiState.availableCenters.forEach { center ->
                           DropdownMenuItem(
                               text = {Text("${center.name}(${center.villageName})")},
                               onClick = {
                                   viewModel.onCenterSelected(center.id)
                                   expanded = false
                               }
                           )
                       }
                   }
               }
           }

           Spacer(modifier = Modifier.weight(1f))

           Button(
               onClick = viewModel::onSaveClicked,
               modifier = Modifier.fillMaxWidth(),
               enabled = uiState.isFormValid && !uiState.isLoading
           ){
              if(uiState.isLoading){
                  CircularProgressIndicator(
                      modifier = Modifier.size(24.dp),
                      color = MaterialTheme.colorScheme.onPrimary
                  )
              }else{
                  Text("Save Lead")
              }
           }
       }
    }
}