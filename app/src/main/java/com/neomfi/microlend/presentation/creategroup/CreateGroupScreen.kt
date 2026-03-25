package com.neomfi.microlend.presentation.creategroup

import androidx.compose.material3.Checkbox
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neomfi.microlend.data.local.entity.LeadEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    onNavigateBack:() -> Unit,
    viewModel: CreateGroupViewModel = hiltViewModel()
){
    val availableLeads by viewModel.unassignedLeads.collectAsStateWithLifecycle()

    var groupName by remember { mutableStateOf("") }

    val selectedLeads = remember {mutableStateListOf<LeadEntity>()}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Form JLG Group")},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack){
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // ---Group Name Input---
            OutlinedTextField(
                value = groupName,
                onValueChange = {groupName = it},
                label = {Text("Group Name")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text ="Select Members (${selectedLeads.size}/5)",
                style = MaterialTheme.typography.titleMedium
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                if(availableLeads.isEmpty()){
                    item { Text("No unassigned leads available.", color = MaterialTheme.colorScheme.error) }
                }

                items(availableLeads){ lead ->
                    val isChecked = selectedLeads.contains(lead)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{
                                if(isChecked){
                                    selectedLeads.remove(lead)
                                }else if(selectedLeads.size<5){
                                    selectedLeads.add(lead)
                                }
                            }.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column{
                            Text(text = lead.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = lead.aadhaarNumber, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.createGroup(groupName = groupName, selectedLeads = selectedLeads, onSuccess = { onNavigateBack()})
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = groupName.isNotBlank()&& selectedLeads.isNotEmpty() && selectedLeads.size <=5
            ){
                Text("Create Group")
            }
        }
    }
}