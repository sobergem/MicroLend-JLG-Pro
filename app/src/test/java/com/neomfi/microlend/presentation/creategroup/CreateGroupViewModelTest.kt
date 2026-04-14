package com.neomfi.microlend.presentation.creategroup

import com.neomfi.microlend.data.local.entity.AssignmentStatus
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import com.neomfi.microlend.domain.usecase.GetUnassignedLeadsUseCase
import com.neomfi.microlend.domain.usecase.InsertGroupUseCase
import com.neomfi.microlend.domain.usecase.UpdateLeadUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateGroupViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var getUnassignedLeadsUseCase: GetUnassignedLeadsUseCase
    private lateinit var insertGroupUseCase: InsertGroupUseCase
    private lateinit var updateLeadUseCase: UpdateLeadUseCase
    private lateinit var viewModel: CreateGroupViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getUnassignedLeadsUseCase = mockk()
        insertGroupUseCase = mockk()
        updateLeadUseCase = mockk()
        every { getUnassignedLeadsUseCase() } returns flowOf(emptyList())
        viewModel = CreateGroupViewModel(getUnassignedLeadsUseCase, insertGroupUseCase, updateLeadUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildLead(id: String, groupId: String? = null) = LeadEntity(
        id = id,
        centerID = "c1",
        groupID = groupId,
        name = "Lead $id",
        aadhaarNumber = "000000000000",
        syncStatus = SyncStatus.PENDING,
        assignmentStatus = AssignmentStatus.UNASSIGNED
    )

    // --- unassignedLeads StateFlow ---

    @Test
    fun unassignedLeads_initialValue_isEmptyList() {
        assertEquals(emptyList<LeadEntity>(), viewModel.unassignedLeads.value)
    }

    @Test
    fun unassignedLeads_reflectsLeadsFromUseCase() = runTest {
        val leads = listOf(buildLead("l1"), buildLead("l2"))
        every { getUnassignedLeadsUseCase() } returns flowOf(leads)

        val vm = CreateGroupViewModel(getUnassignedLeadsUseCase, insertGroupUseCase, updateLeadUseCase)

        assertEquals(leads, vm.unassignedLeads.value)
    }

    // --- createGroup ---

    @Test
    fun createGroup_insertsGroupWithCorrectName() = runTest {
        val groupSlot = slot<JlgGroupEntity>()
        coEvery { insertGroupUseCase(capture(groupSlot)) } returns Unit

        viewModel.createGroup("Alpha Group", emptyList()) {}

        assertEquals("Alpha Group", groupSlot.captured.name)
    }

    @Test
    fun createGroup_insertsGroupWithHardcodedCenterId() = runTest {
        val groupSlot = slot<JlgGroupEntity>()
        coEvery { insertGroupUseCase(capture(groupSlot)) } returns Unit

        viewModel.createGroup("Test Group", emptyList()) {}

        assertEquals("CENTER_123", groupSlot.captured.centerID)
    }

    @Test
    fun createGroup_insertsGroupWithPendingSyncStatus() = runTest {
        val groupSlot = slot<JlgGroupEntity>()
        coEvery { insertGroupUseCase(capture(groupSlot)) } returns Unit

        viewModel.createGroup("Beta Group", emptyList()) {}

        assertEquals(SyncStatus.PENDING, groupSlot.captured.syncStatus)
    }

    @Test
    fun createGroup_generatesNonEmptyGroupId() = runTest {
        val groupSlot = slot<JlgGroupEntity>()
        coEvery { insertGroupUseCase(capture(groupSlot)) } returns Unit

        viewModel.createGroup("Gamma Group", emptyList()) {}

        assertTrue(groupSlot.captured.id.isNotEmpty())
    }

    @Test
    fun createGroup_updatesEachSelectedLeadWithNewGroupId() = runTest {
        val lead1 = buildLead("l1")
        val lead2 = buildLead("l2")
        val updatedLeads = mutableListOf<LeadEntity>()
        val groupSlot = slot<JlgGroupEntity>()

        coEvery { insertGroupUseCase(capture(groupSlot)) } returns Unit
        coEvery { updateLeadUseCase(capture(mutableListOf<LeadEntity>().also { updatedLeads.addAll(it) })) } returns Unit
        coEvery { updateLeadUseCase(any()) } coAnswers {
            updatedLeads.add(firstArg())
        }

        viewModel.createGroup("Group X", listOf(lead1, lead2)) {}

        assertEquals(2, updatedLeads.size)
        val capturedGroupId = groupSlot.captured.id
        assertTrue(updatedLeads.all { it.groupID == capturedGroupId })
    }

    @Test
    fun createGroup_updatedLeadsRetainOriginalData() = runTest {
        val lead = buildLead("l-original")
        val updatedLeads = mutableListOf<LeadEntity>()
        val groupSlot = slot<JlgGroupEntity>()

        coEvery { insertGroupUseCase(capture(groupSlot)) } returns Unit
        coEvery { updateLeadUseCase(any()) } coAnswers {
            updatedLeads.add(firstArg())
        }

        viewModel.createGroup("Preserve Group", listOf(lead)) {}

        val updatedLead = updatedLeads[0]
        assertEquals("l-original", updatedLead.id)
        assertEquals(lead.name, updatedLead.name)
        assertEquals(lead.aadhaarNumber, updatedLead.aadhaarNumber)
        assertNotNull(updatedLead.groupID)
    }

    @Test
    fun createGroup_noSelectedLeads_doesNotCallUpdateLead() = runTest {
        coEvery { insertGroupUseCase(any()) } returns Unit

        viewModel.createGroup("Empty Group", emptyList()) {}

        coVerify(exactly = 0) { updateLeadUseCase(any()) }
    }

    @Test
    fun createGroup_callsOnSuccessAfterOperations() = runTest {
        coEvery { insertGroupUseCase(any()) } returns Unit
        var successCalled = false

        viewModel.createGroup("Success Group", emptyList()) { successCalled = true }

        assertTrue(successCalled)
    }

    @Test
    fun createGroup_multipleLeads_eachLeadGetsNewGroupId() = runTest {
        val leads = listOf(buildLead("l1"), buildLead("l2"), buildLead("l3"))
        val capturedLeads = mutableListOf<LeadEntity>()
        val groupSlot = slot<JlgGroupEntity>()

        coEvery { insertGroupUseCase(capture(groupSlot)) } returns Unit
        coEvery { updateLeadUseCase(any()) } coAnswers {
            capturedLeads.add(firstArg())
        }

        viewModel.createGroup("Multi Group", leads) {}

        assertEquals(3, capturedLeads.size)
        val newGroupId = groupSlot.captured.id
        capturedLeads.forEach { lead ->
            assertEquals(newGroupId, lead.groupID)
        }
    }

    @Test
    fun createGroup_twoGroupsCreated_haveDifferentIds() = runTest {
        val groupIds = mutableListOf<String>()
        coEvery { insertGroupUseCase(any()) } coAnswers {
            groupIds.add(firstArg<JlgGroupEntity>().id)
        }

        viewModel.createGroup("First Group", emptyList()) {}
        viewModel.createGroup("Second Group", emptyList()) {}

        assertEquals(2, groupIds.size)
        assertTrue(groupIds[0] != groupIds[1])
    }
}