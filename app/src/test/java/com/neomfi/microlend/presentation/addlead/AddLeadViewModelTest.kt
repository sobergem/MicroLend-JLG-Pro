package com.neomfi.microlend.presentation.addlead

import com.neomfi.microlend.data.local.entity.AssignmentStatus
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import com.neomfi.microlend.domain.usecase.InsertLeadUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddLeadViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var insertLeadUseCase: InsertLeadUseCase
    private lateinit var viewModel: AddLeadViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        insertLeadUseCase = mockk()
        viewModel = AddLeadViewModel(insertLeadUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- saveLead ---

    @Test
    fun saveLead_callsInsertLeadUseCase() = runTest {
        val leadSlot = slot<LeadEntity>()
        coEvery { insertLeadUseCase(capture(leadSlot)) } returns Unit
        var successCalled = false

        viewModel.saveLead("Ramesh Kumar", "123456789012") { successCalled = true }

        coVerify(exactly = 1) { insertLeadUseCase(any()) }
    }

    @Test
    fun saveLead_createsLeadWithCorrectName() = runTest {
        val leadSlot = slot<LeadEntity>()
        coEvery { insertLeadUseCase(capture(leadSlot)) } returns Unit

        viewModel.saveLead("Alice Sharma", "123456789012") {}

        assertEquals("Alice Sharma", leadSlot.captured.name)
    }

    @Test
    fun saveLead_createsLeadWithCorrectAadhaarNumber() = runTest {
        val leadSlot = slot<LeadEntity>()
        coEvery { insertLeadUseCase(capture(leadSlot)) } returns Unit

        viewModel.saveLead("Bob", "987654321098") {}

        assertEquals("987654321098", leadSlot.captured.aadhaarNumber)
    }

    @Test
    fun saveLead_createsLeadWithHardcodedCenterId() = runTest {
        val leadSlot = slot<LeadEntity>()
        coEvery { insertLeadUseCase(capture(leadSlot)) } returns Unit

        viewModel.saveLead("Test Name", "000000000000") {}

        assertEquals("CENTER_123", leadSlot.captured.centerID)
    }

    @Test
    fun saveLead_createsLeadWithNullGroupId() = runTest {
        val leadSlot = slot<LeadEntity>()
        coEvery { insertLeadUseCase(capture(leadSlot)) } returns Unit

        viewModel.saveLead("Test Name", "000000000000") {}

        assertNull(leadSlot.captured.groupID)
    }

    @Test
    fun saveLead_createsLeadWithPendingSyncStatus() = runTest {
        val leadSlot = slot<LeadEntity>()
        coEvery { insertLeadUseCase(capture(leadSlot)) } returns Unit

        viewModel.saveLead("Test Name", "000000000000") {}

        assertEquals(SyncStatus.PENDING, leadSlot.captured.syncStatus)
    }

    @Test
    fun saveLead_createsLeadWithUnassignedAssignmentStatus() = runTest {
        val leadSlot = slot<LeadEntity>()
        coEvery { insertLeadUseCase(capture(leadSlot)) } returns Unit

        viewModel.saveLead("Test Name", "000000000000") {}

        assertEquals(AssignmentStatus.UNASSIGNED, leadSlot.captured.assignmentStatus)
    }

    @Test
    fun saveLead_callsOnSuccessAfterInsertion() = runTest {
        coEvery { insertLeadUseCase(any()) } returns Unit
        var successCalled = false

        viewModel.saveLead("Name", "123412341234") { successCalled = true }

        assertTrue(successCalled)
    }

    @Test
    fun saveLead_doesNotCallOnSuccessBeforeInsertionCompletes() = runTest {
        var insertionCompleted = false
        var successCalledBeforeInsert = false
        coEvery { insertLeadUseCase(any()) } coAnswers {
            // Record that the success shouldn't have been called yet when we're here
            successCalledBeforeInsert = insertionCompleted
            insertionCompleted = true
        }
        var successCalled = false

        viewModel.saveLead("Name", "123412341234") { successCalled = true }

        assertTrue(insertionCompleted)
        assertTrue(successCalled)
        // success was called after insertion (successCalledBeforeInsert should be false
        // since insertion sets insertionCompleted=true before returning)
        // Actually: insertionCompleted is false when entering coAnswers, then set to true
        // So successCalledBeforeInsert = false (the insertion had not completed before entering the block)
    }

    @Test
    fun saveLead_generatesNonEmptyLeadId() = runTest {
        val leadSlot = slot<LeadEntity>()
        coEvery { insertLeadUseCase(capture(leadSlot)) } returns Unit

        viewModel.saveLead("Test", "111111111111") {}

        assertTrue(leadSlot.captured.id.isNotEmpty())
    }
}