package com.neomfi.microlend.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("MicroLend JLG Pro")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ){
        paddingValues ->
        Box(
            modifier= Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            when(val state = uiState){
                is DashboardUiState.Loading ->{
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DashboardUiState.Error ->{
                    Text(
                        text = "Error: $(state.message)",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DashboardUiState.Success ->{
                    DashboardContent(
                        groups = state.groups,
                        unassignedLeads = state.unassignedLeads
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardContent(
    groups: List<JlgGroupEntity>,
    unassignedLeads: List<LeadEntity>
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        item{
            Text(
                text = "Active JLG Groups",
                style = MaterialTheme.typography.titleLarge
            )
        }

        if(groups.isEmpty()){
            item{ Text("No groups created yet.", style = MaterialTheme.typography.bodyMedium)}
        }else{
           items(groups){group->
               Card(modifier = Modifier.fillMaxWidth()){
                   Text(
                       text = "Group: ${group.name}",
                       modifier = Modifier.padding(16.dp),
                       style = MaterialTheme.typography.titleMedium
                   )
               }
           }
        }
        item{
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Unassigned Leads",
                style = MaterialTheme.typography.titleLarge
            )
        }
        if(unassignedLeads.isEmpty()){
            item{ Text("No pending leads.", style = MaterialTheme.typography.bodyMedium)}
        }else{
            items(unassignedLeads){unassignedLead->
                Card(modifier = Modifier.fillMaxWidth()){
                    Text(
                        text = "Lead: ${unassignedLead.name}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

