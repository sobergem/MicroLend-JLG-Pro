package com.neomfi.microlend.presentation.addlead

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLeadScreen(
    onNavigationBack: () -> Unit,
    viewModel : AddLeadViewModel = hiltViewModel()
){
    var name by remember {mutableStateOf("")}
    var aadhaarNumber by remember {mutableStateOf("")}

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
               value = name,
               onValueChange = {newText -> name = newText},
               label = {Text("Full Name")},
               modifier = Modifier.fillMaxWidth(),
               singleLine = true
           )

           //Aadhaar Field
           OutlinedTextField(
               value = aadhaarNumber,
               onValueChange = { newText ->
                   if(newText.length <=12 && newText.all { it.isDigit()}){
                       aadhaarNumber = newText
                   }
               },
               label = { Text("Aadhaar Number (12 Digits)") },
               modifier = Modifier.fillMaxWidth(),
               singleLine = true,
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
           )

           Spacer(modifier = Modifier.weight(1f))

           Button(
               onClick = {
                  viewModel.saveLead(
                      name = name,
                      aadhaarNumber = aadhaarNumber,
                      onSuccess = {
                          onNavigationBack()
                      }
                  )
               },
               modifier = Modifier.fillMaxWidth(),
               enabled = name.isNotBlank() && aadhaarNumber.length == 12
           ){
               Text("Save Lead")
           }
       }
    }
}