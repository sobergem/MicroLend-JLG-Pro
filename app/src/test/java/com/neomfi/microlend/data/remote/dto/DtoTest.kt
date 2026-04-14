package com.neomfi.microlend.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DtoTest {

    // --- GroupDto ---

    @Test
    fun groupDto_dataClassEquality() {
        val dto1 = GroupDto(groupId = "g1", centerId = "c1", groupName = "Alpha", isComplete = true)
        val dto2 = GroupDto(groupId = "g1", centerId = "c1", groupName = "Alpha", isComplete = true)
        assertEquals(dto1, dto2)
    }

    @Test
    fun groupDto_copy_updatesField() {
        val dto = GroupDto(groupId = "g1", centerId = "c1", groupName = "Alpha", isComplete = false)
        val updated = dto.copy(isComplete = true)
        assertTrue(updated.isComplete)
        assertEquals(dto.groupId, updated.groupId)
    }

    @Test
    fun groupDto_fieldsAreCorrectlySet() {
        val dto = GroupDto(
            groupId = "grp-123",
            centerId = "ctr-456",
            groupName = "Beta Group",
            isComplete = true
        )
        assertEquals("grp-123", dto.groupId)
        assertEquals("ctr-456", dto.centerId)
        assertEquals("Beta Group", dto.groupName)
        assertTrue(dto.isComplete)
    }

    // --- LeadDto ---

    @Test
    fun leadDto_dataClassEquality() {
        val dto1 = LeadDto(leadId = "l1", groupId = "g1", fullName = "Alice", aadhaarNo = "123412341234", syncStatus = "PENDING")
        val dto2 = LeadDto(leadId = "l1", groupId = "g1", fullName = "Alice", aadhaarNo = "123412341234", syncStatus = "PENDING")
        assertEquals(dto1, dto2)
    }

    @Test
    fun leadDto_nullGroupId_isAllowed() {
        val dto = LeadDto(leadId = "l1", groupId = null, fullName = "Bob", aadhaarNo = "000000000000", syncStatus = "PENDING")
        assertNull(dto.groupId)
    }

    @Test
    fun leadDto_fieldsAreCorrectlySet() {
        val dto = LeadDto(
            leadId = "lead-999",
            groupId = "grp-001",
            fullName = "Ramesh Kumar",
            aadhaarNo = "987654321098",
            syncStatus = "SYNCED"
        )
        assertEquals("lead-999", dto.leadId)
        assertEquals("grp-001", dto.groupId)
        assertEquals("Ramesh Kumar", dto.fullName)
        assertEquals("987654321098", dto.aadhaarNo)
        assertEquals("SYNCED", dto.syncStatus)
    }

    @Test
    fun leadDto_copy_updatesGroupId() {
        val dto = LeadDto(leadId = "l1", groupId = null, fullName = "Alice", aadhaarNo = "111111111111", syncStatus = "PENDING")
        val updated = dto.copy(groupId = "grp-new")
        assertEquals("grp-new", updated.groupId)
        assertEquals(dto.leadId, updated.leadId)
    }

    // --- BulkSyncRequest ---

    @Test
    fun bulkSyncRequest_dataClassEquality() {
        val groups = listOf(GroupDto("g1", "c1", "Alpha", true))
        val leads = listOf(LeadDto("l1", null, "Alice", "111111111111", "PENDING"))
        val req1 = BulkSyncRequest(groups = groups, leads = leads)
        val req2 = BulkSyncRequest(groups = groups, leads = leads)
        assertEquals(req1, req2)
    }

    @Test
    fun bulkSyncRequest_emptyLists_isAllowed() {
        val req = BulkSyncRequest(groups = emptyList(), leads = emptyList())
        assertEquals(0, req.groups.size)
        assertEquals(0, req.leads.size)
    }

    @Test
    fun bulkSyncRequest_fieldsAreCorrectlySet() {
        val groups = listOf(
            GroupDto("g1", "c1", "Alpha", true),
            GroupDto("g2", "c1", "Beta", false)
        )
        val leads = listOf(
            LeadDto("l1", "g1", "Alice", "111111111111", "PENDING"),
            LeadDto("l2", "g2", "Bob", "222222222222", "SYNCED")
        )
        val req = BulkSyncRequest(groups = groups, leads = leads)
        assertEquals(2, req.groups.size)
        assertEquals(2, req.leads.size)
        assertEquals("g1", req.groups[0].groupId)
        assertEquals("l2", req.leads[1].leadId)
    }
}