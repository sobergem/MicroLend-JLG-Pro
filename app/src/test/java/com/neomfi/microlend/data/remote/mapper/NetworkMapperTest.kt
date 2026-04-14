package com.neomfi.microlend.data.remote.mapper

import com.neomfi.microlend.data.local.entity.AssignmentStatus
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkMapperTest {

    // --- LeadEntity.toDto() ---

    @Test
    fun leadEntityToDto_mapsIdToLeadId() {
        val lead = buildLead(id = "lead-001")
        assertEquals("lead-001", lead.toDto().leadId)
    }

    @Test
    fun leadEntityToDto_mapsGroupIdWhenPresent() {
        val lead = buildLead(groupId = "group-abc")
        assertEquals("group-abc", lead.toDto().groupId)
    }

    @Test
    fun leadEntityToDto_mapsNullGroupId() {
        val lead = buildLead(groupId = null)
        assertNull(lead.toDto().groupId)
    }

    @Test
    fun leadEntityToDto_mapsNameToFullName() {
        val lead = buildLead(name = "Ramesh Kumar")
        assertEquals("Ramesh Kumar", lead.toDto().fullName)
    }

    @Test
    fun leadEntityToDto_mapsAadhaarNumberToAadhaarNo() {
        val lead = buildLead(aadhaarNumber = "123456789012")
        assertEquals("123456789012", lead.toDto().aadhaarNo)
    }

    @Test
    fun leadEntityToDto_mapsSyncStatusNameToString() {
        val lead = buildLead(syncStatus = SyncStatus.PENDING)
        assertEquals("PENDING", lead.toDto().syncStatus)
    }

    @Test
    fun leadEntityToDto_syncedStatus_mapsCorrectly() {
        val lead = buildLead(syncStatus = SyncStatus.SYNCED)
        assertEquals("SYNCED", lead.toDto().syncStatus)
    }

    @Test
    fun leadEntityToDto_failedStatus_mapsCorrectly() {
        val lead = buildLead(syncStatus = SyncStatus.FAILED)
        assertEquals("FAILED", lead.toDto().syncStatus)
    }

    @Test
    fun leadEntityToDto_allSyncStatusValues_mapToName() {
        SyncStatus.entries.forEach { status ->
            val lead = buildLead(syncStatus = status)
            assertEquals(status.name, lead.toDto().syncStatus)
        }
    }

    // --- JlgGroupEntity.toDto() ---

    @Test
    fun jlgGroupEntityToDto_mapsIdToGroupId() {
        val group = buildGroup(id = "grp-999")
        assertEquals("grp-999", group.toDto().groupId)
    }

    @Test
    fun jlgGroupEntityToDto_mapsCenterIdToCenterId() {
        val group = buildGroup(centerId = "center-X1")
        assertEquals("center-X1", group.toDto().centerId)
    }

    @Test
    fun jlgGroupEntityToDto_mapsNameToGroupName() {
        val group = buildGroup(name = "Alpha Group")
        assertEquals("Alpha Group", group.toDto().groupName)
    }

    @Test
    fun jlgGroupEntityToDto_isCompleteAlwaysTrue() {
        val pendingGroup = buildGroup(syncStatus = SyncStatus.PENDING)
        val syncedGroup = buildGroup(syncStatus = SyncStatus.SYNCED)
        assertTrue(pendingGroup.toDto().isComplete)
        assertTrue(syncedGroup.toDto().isComplete)
    }

    @Test
    fun leadEntityToDto_fullMapping_allFieldsCorrect() {
        val lead = LeadEntity(
            id = "full-lead-id",
            centerID = "ctr-1",
            groupID = "grp-1",
            name = "Jane Doe",
            aadhaarNumber = "987654321098",
            syncStatus = SyncStatus.SYNCING,
            assignmentStatus = AssignmentStatus.ASSIGNED
        )
        val dto = lead.toDto()
        assertEquals("full-lead-id", dto.leadId)
        assertEquals("grp-1", dto.groupId)
        assertEquals("Jane Doe", dto.fullName)
        assertEquals("987654321098", dto.aadhaarNo)
        assertEquals("SYNCING", dto.syncStatus)
    }

    @Test
    fun jlgGroupEntityToDto_fullMapping_allFieldsCorrect() {
        val group = JlgGroupEntity(
            id = "full-group-id",
            centerID = "ctr-2",
            name = "Beta Group",
            syncStatus = SyncStatus.FAILED
        )
        val dto = group.toDto()
        assertEquals("full-group-id", dto.groupId)
        assertEquals("ctr-2", dto.centerId)
        assertEquals("Beta Group", dto.groupName)
        assertTrue(dto.isComplete)
    }

    // --- Helpers ---

    private fun buildLead(
        id: String = "default-id",
        groupId: String? = "default-group",
        name: String = "Default Name",
        aadhaarNumber: String = "000000000000",
        syncStatus: SyncStatus = SyncStatus.PENDING,
        assignmentStatus: AssignmentStatus = AssignmentStatus.UNASSIGNED
    ) = LeadEntity(
        id = id,
        centerID = "CENTER_123",
        groupID = groupId,
        name = name,
        aadhaarNumber = aadhaarNumber,
        syncStatus = syncStatus,
        assignmentStatus = assignmentStatus
    )

    private fun buildGroup(
        id: String = "default-group-id",
        centerId: String = "CENTER_123",
        name: String = "Default Group",
        syncStatus: SyncStatus = SyncStatus.PENDING
    ) = JlgGroupEntity(
        id = id,
        centerID = centerId,
        name = name,
        syncStatus = syncStatus
    )
}