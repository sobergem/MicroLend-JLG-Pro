package com.neomfi.microlend.presentation.navigation

import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import com.neomfi.microlend.presentation.dashboard.DashboardUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScreenTest {

    // --- Screen sealed class routes ---

    @Test
    fun screen_dashboard_hasCorrectRoute() {
        assertEquals("dashboard_screen", Screen.Dashboard.route)
    }

    @Test
    fun screen_addLead_hasCorrectRoute() {
        assertEquals("add_lead_screen", Screen.AddLead.route)
    }

    @Test
    fun screen_createGroup_hasCorrectRoute() {
        assertEquals("create_group_screen", Screen.CreateGroup.route)
    }

    @Test
    fun screen_routesAreAllDistinct() {
        val routes = listOf(
            Screen.Dashboard.route,
            Screen.AddLead.route,
            Screen.CreateGroup.route
        )
        assertEquals(routes.size, routes.toSet().size)
    }

    @Test
    fun screen_routesAreNotEmpty() {
        assertTrue(Screen.Dashboard.route.isNotEmpty())
        assertTrue(Screen.AddLead.route.isNotEmpty())
        assertTrue(Screen.CreateGroup.route.isNotEmpty())
    }

    // --- DashboardUiState ---

    @Test
    fun dashboardUiState_loading_isDistinctType() {
        val state: DashboardUiState = DashboardUiState.Loading
        assertTrue(state is DashboardUiState.Loading)
    }

    @Test
    fun dashboardUiState_error_containsMessage() {
        val state = DashboardUiState.Error("Something went wrong")
        assertEquals("Something went wrong", state.message)
    }

    @Test
    fun dashboardUiState_success_containsGroupsAndLeads() {
        val groups = listOf(
            JlgGroupEntity(id = "g1", centerID = "c1", name = "Alpha", syncStatus = SyncStatus.PENDING)
        )
        val leads = listOf<LeadEntity>()
        val state = DashboardUiState.Success(
            groups = groups,
            unassignedLeads = leads,
            hasUnSyncedData = true
        )
        assertEquals(1, state.groups.size)
        assertEquals(0, state.unassignedLeads.size)
        assertTrue(state.hasUnSyncedData)
    }

    @Test
    fun dashboardUiState_success_hasUnSyncedDataFalse() {
        val state = DashboardUiState.Success(
            groups = emptyList(),
            unassignedLeads = emptyList(),
            hasUnSyncedData = false
        )
        assertFalse(state.hasUnSyncedData)
    }

    @Test
    fun dashboardUiState_error_dataClassEquality() {
        val e1 = DashboardUiState.Error("err")
        val e2 = DashboardUiState.Error("err")
        assertEquals(e1, e2)
    }

    @Test
    fun dashboardUiState_success_dataClassEquality() {
        val s1 = DashboardUiState.Success(emptyList(), emptyList(), false)
        val s2 = DashboardUiState.Success(emptyList(), emptyList(), false)
        assertEquals(s1, s2)
    }

    @Test
    fun dashboardUiState_error_differentMessages_notEqual() {
        val e1 = DashboardUiState.Error("error 1")
        val e2 = DashboardUiState.Error("error 2")
        assertTrue(e1 != e2)
    }
}