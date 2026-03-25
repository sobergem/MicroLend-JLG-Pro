package com.neomfi.microlend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.neomfi.microlend.presentation.dashboard.DashboardScreen
import com.neomfi.microlend.ui.theme.MicroLendJLGProTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
          MicroLendJLGProTheme{
              DashboardScreen()
          }
        }
    }
}

