package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.data.local.entity.AssignmentStatus
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import com.neomfi.microlend.domain.repository.JlgGroupRepository
import com.neomfi.microlend.domain.repository.LeadRepository
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

class UseCaseTest {

    private lateinit var leadRepository: LeadRepository
    private lateinit var groupRepository: JlgGroupRepository

    @Before
    fun setUp() {
        leadRepository = mockk()
        groupRepository = mockk()
    }

    private fun buildLead(id: String = "l1", groupId: String? = null) = LeadEntity(
        id = id,
        centerID = "c1",
        groupID = groupId,
        name = "Test Lead",
        aadhaarNumber = "000000000000",
        syncStatus = SyncStatus.PENDING,
        assignmentStatus = AssignmentStatus.UNASSIGNED
    )

    private fun buildGroup(id: String = "g1") = JlgGroupEntity(
        id = id,
        centerID = "c1",
        name = "Test Group",
        syncStatus = SyncStatus.PENDING
    )

    // --- InsertLeadUseCase ---

    @Test
    fun insertLeadUseCase_invokeDelegatesToRepository() = runTest {
        val useCase = InsertLeadUseCase(leadRepository)
        val lead = buildLead("l1")
        coEvery { leadRepository.insertLead(lead) } returns Unit

        useCase(lead)

        coVerify(exactly = 1) { leadRepository.insertLead(lead) }
    }

    @Test
    fun insertLeadUseCase_differentLeads_eachDelegatesOnce() = runTest {
        val useCase = InsertLeadUseCase(leadRepository)
        val lead1 = buildLead("l1")
        val lead2 = buildLead("l2")
        coEvery { leadRepository.insertLead(any()) } returns Unit

        useCase(lead1)
        useCase(lead2)

        coVerify(exactly = 1) { leadRepository.insertLead(lead1) }
        coVerify(exactly = 1) { leadRepository.insertLead(lead2) }
    }

    // --- UpdateLeadUseCase ---

    @Test
    fun updateLeadUseCase_invokeDelegatesToRepository() = runTest {
        val useCase = UpdateLeadUseCase(leadRepository)
        val lead = buildLead("l1", groupId = "g1")
        coEvery { leadRepository.updateLead(lead) } returns Unit

        useCase(lead)

        coVerify(exactly = 1) { leadRepository.updateLead(lead) }
    }

    // --- GetUnassignedLeadsUseCase ---

    @Test
    fun getUnassignedLeadsUseCase_invokeDelegatesToRepository() = runTest {
        val useCase = GetUnassignedLeadsUseCase(leadRepository)
        val leads = listOf(buildLead("l1"), buildLead("l2"))
        every { leadRepository.getUnassignedLeads() } returns flowOf(leads)

        val result = useCase().first()

        assertEquals(leads, result)
        verify(exactly = 1) { leadRepository.getUnassignedLeads() }
    }

    @Test
    fun getUnassignedLeadsUseCase_emptyRepository_returnsEmptyList() = runTest {
        val useCase = GetUnassignedLeadsUseCase(leadRepository)
        every { leadRepository.getUnassignedLeads() } returns flowOf(emptyList())

        val result = useCase().first()

        assertEquals(emptyList<LeadEntity>(), result)
    }

    // --- InsertGroupUseCase ---

    @Test
    fun insertGroupUseCase_invokeDelegatesToRepository() = runTest {
        val useCase = InsertGroupUseCase(groupRepository)
        val group = buildGroup("g1")
        coEvery { groupRepository.insertGroup(group) } returns Unit

        useCase(group)

        coVerify(exactly = 1) { groupRepository.insertGroup(group) }
    }

    // --- GetGroupsByCenterUseCase ---

    @Test
    fun getGroupsByCenterUseCase_passesCorrectCenterIdToRepository() = runTest {
        val useCase = GetGroupsByCenterUseCase(groupRepository)
        val centerId = "CTR-XYZ"
        val groups = listOf(buildGroup("g1"), buildGroup("g2"))
        every { groupRepository.getGroupsByCenter(centerId) } returns flowOf(groups)

        val result = useCase(centerId).first()

        assertEquals(groups, result)
        verify(exactly = 1) { groupRepository.getGroupsByCenter(centerId) }
    }

    @Test
    fun getGroupsByCenterUseCase_emptyCenter_returnsEmptyList() = runTest {
        val useCase = GetGroupsByCenterUseCase(groupRepository)
        val centerId = "EMPTY_CTR"
        every { groupRepository.getGroupsByCenter(centerId) } returns flowOf(emptyList())

        val result = useCase(centerId).first()

        assertEquals(emptyList<JlgGroupEntity>(), result)
    }

    @Test
    fun getGroupsByCenterUseCase_differentCenterIds_queryEachSeparately() = runTest {
        val useCase = GetGroupsByCenterUseCase(groupRepository)
        val center1 = "CTR-1"
        val center2 = "CTR-2"
        every { groupRepository.getGroupsByCenter(center1) } returns flowOf(listOf(buildGroup("g1")))
        every { groupRepository.getGroupsByCenter(center2) } returns flowOf(listOf(buildGroup("g2"), buildGroup("g3")))

        val result1 = useCase(center1).first()
        val result2 = useCase(center2).first()

        assertEquals(1, result1.size)
        assertEquals(2, result2.size)
    }
}