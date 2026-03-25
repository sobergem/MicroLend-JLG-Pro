package com.neomfi.microlend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neomfi.microlend.presentation.addlead.AddLeadScreen
import com.neomfi.microlend.presentation.creategroup.CreateGroupScreen
import com.neomfi.microlend.presentation.dashboard.DashboardScreen
import com.neomfi.microlend.presentation.navigation.Screen
import com.neomfi.microlend.ui.theme.MicroLendJLGProTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
          MicroLendJLGProTheme{
              val navController = rememberNavController()

              NavHost(
                  navController = navController,
                  startDestination = Screen.Dashboard.route
              ){
                  composable(route = Screen.Dashboard.route){
                      DashboardScreen(
                          onNavigateToAddLead ={
                              navController.navigate(Screen.AddLead.route)
                          },
                          onNavigateToCreateGroup = {
                              navController.navigate(Screen.CreateGroup.route)
                          }
                      )
                  }

                  composable(route = Screen.AddLead.route){
                      AddLeadScreen(
                          onNavigationBack = {
                          navController.popBackStack()
                      })
                  }

                  composable(route = Screen.CreateGroup.route){
                      CreateGroupScreen(
                          onNavigateBack = {
                              navController.popBackStack()
                          }
                      )
                  }
              }
          }
        }
    }
}

