package com.neomfi.microlend.data.repository

import com.neomfi.microlend.data.dao.JlgGroupDao
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.SyncStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class JlgGroupRepositoryImplTest {

    private lateinit var dao: JlgGroupDao
    private lateinit var repository: JlgGroupRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk()
        repository = JlgGroupRepositoryImpl(dao)
    }

    // --- getGroupsByCenter ---

    @Test
    fun getGroupsByCenter_delegatesToDao() = runTest {
        val centerId = "CTR-1"
        val groups = listOf(
            JlgGroupEntity(id = "g1", centerID = centerId, name = "Alpha", syncStatus = SyncStatus.PENDING)
        )
        every { dao.getGroupsByCenter(centerId) } returns flowOf(groups)

        val result = repository.getGroupsByCenter(centerId).first()

        assertEquals(groups, result)
        verify(exactly = 1) { dao.getGroupsByCenter(centerId) }
    }

    @Test
    fun getGroupsByCenter_emptyList_returnsEmptyFlow() = runTest {
        val centerId = "CTR-EMPTY"
        every { dao.getGroupsByCenter(centerId) } returns flowOf(emptyList())

        val result = repository.getGroupsByCenter(centerId).first()

        assertEquals(emptyList<JlgGroupEntity>(), result)
    }

    @Test
    fun getGroupsByCenter_multipleGroups_returnsAll() = runTest {
        val centerId = "CTR-2"
        val groups = listOf(
            JlgGroupEntity(id = "g1", centerID = centerId, name = "Alpha", syncStatus = SyncStatus.PENDING),
            JlgGroupEntity(id = "g2", centerID = centerId, name = "Beta", syncStatus = SyncStatus.SYNCED)
        )
        every { dao.getGroupsByCenter(centerId) } returns flowOf(groups)

        val result = repository.getGroupsByCenter(centerId).first()

        assertEquals(2, result.size)
    }

    // --- insertGroup ---

    @Test
    fun insertGroup_delegatesToDao() = runTest {
        val group = JlgGroupEntity(id = "g1", centerID = "c1", name = "Gamma", syncStatus = SyncStatus.PENDING)
        coEvery { dao.insertGroup(group) } returns Unit

        repository.insertGroup(group)

        coVerify(exactly = 1) { dao.insertGroup(group) }
    }

    // --- deleteGroup ---

    @Test
    fun deleteGroup_delegatesToDao() = runTest {
        val group = JlgGroupEntity(id = "g1", centerID = "c1", name = "Delta", syncStatus = SyncStatus.SYNCED)
        coEvery { dao.deleteGroup(group) } returns Unit

        repository.deleteGroup(group)

        coVerify(exactly = 1) { dao.deleteGroup(group) }
    }

    @Test
    fun insertGroup_thenDelete_daoCalledInOrder() = runTest {
        val group = JlgGroupEntity(id = "g1", centerID = "c1", name = "Epsilon", syncStatus = SyncStatus.PENDING)
        coEvery { dao.insertGroup(group) } returns Unit
        coEvery { dao.deleteGroup(group) } returns Unit

        repository.insertGroup(group)
        repository.deleteGroup(group)

        coVerify(exactly = 1) { dao.insertGroup(group) }
        coVerify(exactly = 1) { dao.deleteGroup(group) }
    }
}