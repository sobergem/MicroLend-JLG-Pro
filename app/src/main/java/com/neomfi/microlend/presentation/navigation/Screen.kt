package com.neomfi.microlend.presentation.navigation

sealed class Screen(val route: String) {
    data object Dashboard: Screen("dashboard_screen")
    data object AddLead : Screen("add_lead_screen")
    object CreateGroup : Screen("create_group_screen/{centerId}"){
        fun passCenterId(centerId:String): String{
            return "create_group_screen/$centerId"
        }
    }
}