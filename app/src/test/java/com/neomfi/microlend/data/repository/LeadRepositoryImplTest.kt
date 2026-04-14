package com.neomfi.microlend.data.repository

import com.neomfi.microlend.data.dao.LeadDao
import com.neomfi.microlend.data.local.entity.AssignmentStatus
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LeadRepositoryImplTest {

    private lateinit var dao: LeadDao
    private lateinit var repository: LeadRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk()
        repository = LeadRepositoryImpl(dao)
    }

    private fun buildLead(
        id: String = "lead-1",
        groupId: String? = null,
        syncStatus: SyncStatus = SyncStatus.PENDING
    ) = LeadEntity(
        id = id,
        centerID = "c1",
        groupID = groupId,
        name = "Test Lead",
        aadhaarNumber = "000000000000",
        syncStatus = syncStatus,
        assignmentStatus = AssignmentStatus.UNASSIGNED
    )

    // --- getAllLeads ---

    @Test
    fun getAllLeads_delegatesToDao() = runTest {
        val leads = listOf(buildLead("l1"), buildLead("l2"))
        every { dao.getAllLeads() } returns flowOf(leads)

        val result = repository.getAllLeads().first()

        assertEquals(leads, result)
        verify(exactly = 1) { dao.getAllLeads() }
    }

    @Test
    fun getAllLeads_empty_returnsEmptyList() = runTest {
        every { dao.getAllLeads() } returns flowOf(emptyList())

        val result = repository.getAllLeads().first()

        assertEquals(emptyList<LeadEntity>(), result)
    }

    // --- getUnassignedLeads ---

    @Test
    fun getUnassignedLeads_delegatesToDao() = runTest {
        val unassigned = listOf(buildLead("l3", groupId = null))
        every { dao.getUnassignedLeads() } returns flowOf(unassigned)

        val result = repository.getUnassignedLeads().first()

        assertEquals(unassigned, result)
        verify(exactly = 1) { dao.getUnassignedLeads() }
    }

    // --- getLeadsByGroup ---

    @Test
    fun getLeadsByGroup_delegatesToDaoWithCorrectGroupId() = runTest {
        val groupId = "grp-1"
        val leads = listOf(buildLead("l4", groupId = groupId))
        every { dao.getLeadsByGroupId(groupId) } returns flowOf(leads)

        val result = repository.getLeadsByGroup(groupId).first()

        assertEquals(leads, result)
        verify(exactly = 1) { dao.getLeadsByGroupId(groupId) }
    }

    @Test
    fun getLeadsByGroup_noLeadsInGroup_returnsEmptyList() = runTest {
        val groupId = "empty-group"
        every { dao.getLeadsByGroupId(groupId) } returns flowOf(emptyList())

        val result = repository.getLeadsByGroup(groupId).first()

        assertEquals(emptyList<LeadEntity>(), result)
    }

    // --- insertLead ---

    @Test
    fun insertLead_delegatesToDao() = runTest {
        val lead = buildLead("l5")
        coEvery { dao.insertLead(lead) } returns Unit

        repository.insertLead(lead)

        coVerify(exactly = 1) { dao.insertLead(lead) }
    }

    // --- updateLead ---

    @Test
    fun updateLead_delegatesToDao() = runTest {
        val lead = buildLead("l6", groupId = "grp-2")
        coEvery { dao.updateLead(lead) } returns Unit

        repository.updateLead(lead)

        coVerify(exactly = 1) { dao.updateLead(lead) }
    }

    // --- deleteLead ---

    @Test
    fun deleteLead_delegatesToDao() = runTest {
        val lead = buildLead("l7")
        coEvery { dao.deleteLead(lead) } returns Unit

        repository.deleteLead(lead)

        coVerify(exactly = 1) { dao.deleteLead(lead) }
    }

    @Test
    fun insertThenDelete_daoCalledForBothOperations() = runTest {
        val lead = buildLead("l8")
        coEvery { dao.insertLead(lead) } returns Unit
        coEvery { dao.deleteLead(lead) } returns Unit

        repository.insertLead(lead)
        repository.deleteLead(lead)

        coVerify(exactly = 1) { dao.insertLead(lead) }
        coVerify(exactly = 1) { dao.deleteLead(lead) }
    }
}