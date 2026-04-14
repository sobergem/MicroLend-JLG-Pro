package com.neomfi.microlend.data.local.entity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class EntitiesTest {

    // --- SyncStatus enum ---

    @Test
    fun syncStatus_hasExactlyFourValues() {
        assertEquals(4, SyncStatus.entries.size)
    }

    @Test
    fun syncStatus_containsExpectedValues() {
        val names = SyncStatus.entries.map { it.name }
        assert("PENDING" in names)
        assert("SYNCING" in names)
        assert("SYNCED" in names)
        assert("FAILED" in names)
    }

    @Test
    fun syncStatus_valueOf_returnsCorrectEnum() {
        assertEquals(SyncStatus.PENDING, SyncStatus.valueOf("PENDING"))
        assertEquals(SyncStatus.SYNCING, SyncStatus.valueOf("SYNCING"))
        assertEquals(SyncStatus.SYNCED, SyncStatus.valueOf("SYNCED"))
        assertEquals(SyncStatus.FAILED, SyncStatus.valueOf("FAILED"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun syncStatus_valueOf_unknownValue_throws() {
        SyncStatus.valueOf("NOT_A_STATUS")
    }

    // --- AssignmentStatus enum ---

    @Test
    fun assignmentStatus_hasExactlyThreeValues() {
        assertEquals(3, AssignmentStatus.entries.size)
    }

    @Test
    fun assignmentStatus_containsExpectedValues() {
        val names = AssignmentStatus.entries.map { it.name }
        assert("UNASSIGNED" in names)
        assert("ASSIGNED" in names)
        assert("REJECTED" in names)
    }

    @Test
    fun assignmentStatus_valueOf_returnsCorrectEnum() {
        assertEquals(AssignmentStatus.UNASSIGNED, AssignmentStatus.valueOf("UNASSIGNED"))
        assertEquals(AssignmentStatus.ASSIGNED, AssignmentStatus.valueOf("ASSIGNED"))
        assertEquals(AssignmentStatus.REJECTED, AssignmentStatus.valueOf("REJECTED"))
    }

    // --- VillageCenterEntity ---

    @Test
    fun villageCenterEntity_dataClassEquality() {
        val center1 = VillageCenterEntity(id = "vc-1", name = "North Center", pinCode = "110001")
        val center2 = VillageCenterEntity(id = "vc-1", name = "North Center", pinCode = "110001")
        assertEquals(center1, center2)
    }

    @Test
    fun villageCenterEntity_differentId_notEqual() {
        val center1 = VillageCenterEntity(id = "vc-1", name = "North Center", pinCode = "110001")
        val center2 = VillageCenterEntity(id = "vc-2", name = "North Center", pinCode = "110001")
        assertNotEquals(center1, center2)
    }

    @Test
    fun villageCenterEntity_copyPreservesFields() {
        val original = VillageCenterEntity(id = "vc-1", name = "South Center", pinCode = "600001")
        val copy = original.copy(pinCode = "600002")
        assertEquals("vc-1", copy.id)
        assertEquals("South Center", copy.name)
        assertEquals("600002", copy.pinCode)
    }

    // --- JlgGroupEntity ---

    @Test
    fun jlgGroupEntity_defaultSyncStatusNotPresent_constructorRequired() {
        val group = JlgGroupEntity(
            id = "grp-1",
            centerID = "ctr-1",
            name = "Group Alpha",
            syncStatus = SyncStatus.PENDING
        )
        assertEquals(SyncStatus.PENDING, group.syncStatus)
    }

    @Test
    fun jlgGroupEntity_dataClassEquality() {
        val g1 = JlgGroupEntity(id = "g1", centerID = "c1", name = "GAlpha", syncStatus = SyncStatus.SYNCED)
        val g2 = JlgGroupEntity(id = "g1", centerID = "c1", name = "GAlpha", syncStatus = SyncStatus.SYNCED)
        assertEquals(g1, g2)
    }

    @Test
    fun jlgGroupEntity_differentSyncStatus_notEqual() {
        val g1 = JlgGroupEntity(id = "g1", centerID = "c1", name = "G", syncStatus = SyncStatus.PENDING)
        val g2 = JlgGroupEntity(id = "g1", centerID = "c1", name = "G", syncStatus = SyncStatus.SYNCED)
        assertNotEquals(g1, g2)
    }

    @Test
    fun jlgGroupEntity_defaultIdGeneratedWhenNotProvided() {
        val g1 = JlgGroupEntity(centerID = "c1", name = "G1", syncStatus = SyncStatus.PENDING)
        val g2 = JlgGroupEntity(centerID = "c1", name = "G1", syncStatus = SyncStatus.PENDING)
        assertNotNull(g1.id)
        assertNotNull(g2.id)
        // Each default id is a distinct UUID
        assertNotEquals(g1.id, g2.id)
    }

    // --- LeadEntity ---

    @Test
    fun leadEntity_defaultSyncStatus_isPending() {
        val lead = LeadEntity(
            id = "l1",
            centerID = "c1",
            groupID = null,
            name = "Test Lead",
            aadhaarNumber = "123456789012"
        )
        assertEquals(SyncStatus.PENDING, lead.syncStatus)
    }

    @Test
    fun leadEntity_defaultAssignmentStatus_isUnassigned() {
        val lead = LeadEntity(
            id = "l1",
            centerID = "c1",
            groupID = null,
            name = "Test Lead",
            aadhaarNumber = "123456789012"
        )
        assertEquals(AssignmentStatus.UNASSIGNED, lead.assignmentStatus)
    }

    @Test
    fun leadEntity_groupIdCanBeNull() {
        val lead = LeadEntity(
            id = "l1",
            centerID = "c1",
            groupID = null,
            name = "Unassigned Lead",
            aadhaarNumber = "123456789012"
        )
        assertNull(lead.groupID)
    }

    @Test
    fun leadEntity_copyUpdatesGroupId() {
        val lead = LeadEntity(
            id = "l1",
            centerID = "c1",
            groupID = null,
            name = "Lead Name",
            aadhaarNumber = "123456789012"
        )
        val updated = lead.copy(groupID = "grp-new")
        assertEquals("grp-new", updated.groupID)
        assertEquals(lead.id, updated.id)
        assertEquals(lead.name, updated.name)
    }

    @Test
    fun leadEntity_dataClassEquality_allFieldsSame() {
        val l1 = LeadEntity(
            id = "l1", centerID = "c1", groupID = "g1",
            name = "Alice", aadhaarNumber = "000000000001",
            syncStatus = SyncStatus.SYNCED, assignmentStatus = AssignmentStatus.ASSIGNED
        )
        val l2 = l1.copy()
        assertEquals(l1, l2)
    }

    @Test
    fun leadEntity_defaultIdIsUuid() {
        val lead = LeadEntity(
            centerID = "c1", groupID = null,
            name = "Bob", aadhaarNumber = "000000000002"
        )
        assertNotNull(lead.id)
        // UUIDs have 36 chars (8-4-4-4-12 with dashes)
        assertEquals(36, lead.id.length)
    }

    // --- GroupWithMembers ---

    @Test
    fun groupWithMembers_holdsGroupAndEmptyMembersList() {
        val group = JlgGroupEntity(id = "g1", centerID = "c1", name = "G", syncStatus = SyncStatus.PENDING)
        val groupWithMembers = GroupWithMembers(group = group, members = emptyList())
        assertEquals(group, groupWithMembers.group)
        assertEquals(0, groupWithMembers.members.size)
    }

    @Test
    fun groupWithMembers_holdsMultipleMembers() {
        val group = JlgGroupEntity(id = "g1", centerID = "c1", name = "G", syncStatus = SyncStatus.PENDING)
        val leads = listOf(
            LeadEntity(id = "l1", centerID = "c1", groupID = "g1", name = "A", aadhaarNumber = "111111111111"),
            LeadEntity(id = "l2", centerID = "c1", groupID = "g1", name = "B", aadhaarNumber = "222222222222")
        )
        val gwm = GroupWithMembers(group = group, members = leads)
        assertEquals(2, gwm.members.size)
    }
}