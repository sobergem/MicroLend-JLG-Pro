package com.neomfi.microlend.data.repository

import android.util.Log
import com.neomfi.microlend.data.dao.JlgGroupDao
import com.neomfi.microlend.data.dao.LeadDao
import com.neomfi.microlend.data.local.entity.AssignmentStatus
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import com.neomfi.microlend.data.remote.api.MicroLendApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class SyncRepositoryImplTest {

    private lateinit var leadDao: LeadDao
    private lateinit var groupDao: JlgGroupDao
    private lateinit var api: MicroLendApi
    private lateinit var repository: SyncRepositoryImpl

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        leadDao = mockk()
        groupDao = mockk()
        api = mockk()
        repository = SyncRepositoryImpl(leadDao, groupDao, api)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    private fun buildLead(id: String, syncStatus: SyncStatus = SyncStatus.PENDING) = LeadEntity(
        id = id,
        centerID = "c1",
        groupID = null,
        name = "Lead $id",
        aadhaarNumber = "000000000000",
        syncStatus = syncStatus,
        assignmentStatus = AssignmentStatus.UNASSIGNED
    )

    private fun buildGroup(id: String, syncStatus: SyncStatus = SyncStatus.PENDING) = JlgGroupEntity(
        id = id,
        centerID = "c1",
        name = "Group $id",
        syncStatus = syncStatus
    )

    // --- No data to sync ---

    @Test
    fun performBulkSync_noPendingData_returnsTrueWithoutCallingApi() = runTest {
        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } returns emptyList()
        coEvery { groupDao.getGroupsBySyncStatus(SyncStatus.PENDING.name) } returns emptyList()

        val result = repository.performBulkSync()

        assertTrue(result)
        coVerify(exactly = 0) { api.syncBulkData(any()) }
    }

    // --- Successful sync ---

    @Test
    fun performBulkSync_hasPendingDataAndApiSucceeds_returnsTrue() = runTest {
        val leads = listOf(buildLead("l1"))
        val groups = listOf(buildGroup("g1"))
        val successResponse = Response.success<Unit>(null)

        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } returns leads
        coEvery { groupDao.getGroupsBySyncStatus(SyncStatus.PENDING.name) } returns groups
        coEvery { api.syncBulkData(any()) } returns successResponse
        coEvery { groupDao.markGroupsAsSynced(any(), SyncStatus.SYNCED) } returns Unit
        coEvery { leadDao.markLeadsAsSynced(any(), SyncStatus.SYNCED) } returns Unit

        val result = repository.performBulkSync()

        assertTrue(result)
    }

    @Test
    fun performBulkSync_success_marksGroupsAndLeadsAsSynced() = runTest {
        val leads = listOf(buildLead("l1"), buildLead("l2"))
        val groups = listOf(buildGroup("g1"), buildGroup("g2"))
        val successResponse = Response.success<Unit>(null)

        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } returns leads
        coEvery { groupDao.getGroupsBySyncStatus(SyncStatus.PENDING.name) } returns groups
        coEvery { api.syncBulkData(any()) } returns successResponse
        coEvery { groupDao.markGroupsAsSynced(listOf("g1", "g2"), SyncStatus.SYNCED) } returns Unit
        coEvery { leadDao.markLeadsAsSynced(listOf("l1", "l2"), SyncStatus.SYNCED) } returns Unit

        repository.performBulkSync()

        coVerify(exactly = 1) { groupDao.markGroupsAsSynced(listOf("g1", "g2"), SyncStatus.SYNCED) }
        coVerify(exactly = 1) { leadDao.markLeadsAsSynced(listOf("l1", "l2"), SyncStatus.SYNCED) }
    }

    // --- Only leads pending ---

    @Test
    fun performBulkSync_onlyLeadsPending_syncsLeadsOnly() = runTest {
        val leads = listOf(buildLead("l1"))
        val successResponse = Response.success<Unit>(null)

        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } returns leads
        coEvery { groupDao.getGroupsBySyncStatus(SyncStatus.PENDING.name) } returns emptyList()
        coEvery { api.syncBulkData(any()) } returns successResponse
        coEvery { leadDao.markLeadsAsSynced(listOf("l1"), SyncStatus.SYNCED) } returns Unit

        val result = repository.performBulkSync()

        assertTrue(result)
        coVerify(exactly = 1) { leadDao.markLeadsAsSynced(listOf("l1"), SyncStatus.SYNCED) }
        coVerify(exactly = 0) { groupDao.markGroupsAsSynced(any(), any()) }
    }

    // --- Only groups pending ---

    @Test
    fun performBulkSync_onlyGroupsPending_syncsGroupsOnly() = runTest {
        val groups = listOf(buildGroup("g1"))
        val successResponse = Response.success<Unit>(null)

        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } returns emptyList()
        coEvery { groupDao.getGroupsBySyncStatus(SyncStatus.PENDING.name) } returns groups
        coEvery { api.syncBulkData(any()) } returns successResponse
        coEvery { groupDao.markGroupsAsSynced(listOf("g1"), SyncStatus.SYNCED) } returns Unit

        val result = repository.performBulkSync()

        assertTrue(result)
        coVerify(exactly = 1) { groupDao.markGroupsAsSynced(listOf("g1"), SyncStatus.SYNCED) }
        coVerify(exactly = 0) { leadDao.markLeadsAsSynced(any(), any()) }
    }

    // --- API failure ---

    @Test
    fun performBulkSync_apiReturnsError_returnsFalse() = runTest {
        val leads = listOf(buildLead("l1"))
        val groups = listOf(buildGroup("g1"))
        val errorResponse = Response.error<Unit>(500, okhttp3.ResponseBody.create(null, ""))

        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } returns leads
        coEvery { groupDao.getGroupsBySyncStatus(SyncStatus.PENDING.name) } returns groups
        coEvery { api.syncBulkData(any()) } returns errorResponse

        val result = repository.performBulkSync()

        assertFalse(result)
    }

    @Test
    fun performBulkSync_apiReturnsError_doesNotMarkAsSynced() = runTest {
        val leads = listOf(buildLead("l1"))
        val groups = listOf(buildGroup("g1"))
        val errorResponse = Response.error<Unit>(400, okhttp3.ResponseBody.create(null, ""))

        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } returns leads
        coEvery { groupDao.getGroupsBySyncStatus(SyncStatus.PENDING.name) } returns groups
        coEvery { api.syncBulkData(any()) } returns errorResponse

        repository.performBulkSync()

        coVerify(exactly = 0) { groupDao.markGroupsAsSynced(any(), any()) }
        coVerify(exactly = 0) { leadDao.markLeadsAsSynced(any(), any()) }
    }

    // --- Exception handling ---

    @Test
    fun performBulkSync_exceptionDuringApiCall_returnsFalse() = runTest {
        val leads = listOf(buildLead("l1"))
        val groups = listOf(buildGroup("g1"))

        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } returns leads
        coEvery { groupDao.getGroupsBySyncStatus(SyncStatus.PENDING.name) } returns groups
        coEvery { api.syncBulkData(any()) } throws RuntimeException("Network failure")

        val result = repository.performBulkSync()

        assertFalse(result)
    }

    @Test
    fun performBulkSync_exceptionDuringDaoQuery_returnsFalse() = runTest {
        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } throws RuntimeException("DB error")

        val result = repository.performBulkSync()

        assertFalse(result)
    }

    // --- API call contains correct data ---

    @Test
    fun performBulkSync_apiCalledWithCorrectBulkRequest() = runTest {
        val leads = listOf(buildLead("lead-id-1"))
        val groups = listOf(buildGroup("group-id-1"))
        val successResponse = Response.success<Unit>(null)

        coEvery { leadDao.getLeadsBySyncStatus(SyncStatus.PENDING.name) } returns leads
        coEvery { groupDao.getGroupsBySyncStatus(SyncStatus.PENDING.name) } returns groups
        coEvery { api.syncBulkData(any()) } returns successResponse
        coEvery { groupDao.markGroupsAsSynced(any(), any()) } returns Unit
        coEvery { leadDao.markLeadsAsSynced(any(), any()) } returns Unit

        repository.performBulkSync()

        coVerify(exactly = 1) {
            api.syncBulkData(match { request ->
                request.groups.size == 1 &&
                    request.groups[0].groupId == "group-id-1" &&
                    request.leads.size == 1 &&
                    request.leads[0].leadId == "lead-id-1"
            })
        }
    }
}