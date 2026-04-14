package com.neomfi.microlend.data.repository

import com.neomfi.microlend.data.dao.VillageCenterDao
import com.neomfi.microlend.data.local.entity.VillageCenterEntity
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

class VillageCenterRepositoryImplTest {

    private lateinit var dao: VillageCenterDao
    private lateinit var repository: VillageCenterRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk()
        repository = VillageCenterRepositoryImpl(dao)
    }

    // --- getAllCenters ---

    @Test
    fun getAllCenters_delegatesToDao() = runTest {
        val centers = listOf(
            VillageCenterEntity(id = "vc1", name = "North Center", pinCode = "110001"),
            VillageCenterEntity(id = "vc2", name = "South Center", pinCode = "600001")
        )
        every { dao.getAllCenters() } returns flowOf(centers)

        val result = repository.getAllCenters().first()

        assertEquals(centers, result)
        verify(exactly = 1) { dao.getAllCenters() }
    }

    @Test
    fun getAllCenters_empty_returnsEmptyList() = runTest {
        every { dao.getAllCenters() } returns flowOf(emptyList())

        val result = repository.getAllCenters().first()

        assertEquals(emptyList<VillageCenterEntity>(), result)
    }

    @Test
    fun getAllCenters_singleCenter_returnsSingleItem() = runTest {
        val center = VillageCenterEntity(id = "vc1", name = "East Center", pinCode = "700001")
        every { dao.getAllCenters() } returns flowOf(listOf(center))

        val result = repository.getAllCenters().first()

        assertEquals(1, result.size)
        assertEquals(center, result[0])
    }

    // --- insertCenter ---

    @Test
    fun insertCenter_delegatesToDao() = runTest {
        val center = VillageCenterEntity(id = "vc3", name = "West Center", pinCode = "400001")
        coEvery { dao.insertCenter(center) } returns Unit

        repository.insertCenter(center)

        coVerify(exactly = 1) { dao.insertCenter(center) }
    }

    @Test
    fun insertCenter_calledMultipleTimes_daoDelegatedForEach() = runTest {
        val c1 = VillageCenterEntity(id = "vc4", name = "C1", pinCode = "111111")
        val c2 = VillageCenterEntity(id = "vc5", name = "C2", pinCode = "222222")
        coEvery { dao.insertCenter(c1) } returns Unit
        coEvery { dao.insertCenter(c2) } returns Unit

        repository.insertCenter(c1)
        repository.insertCenter(c2)

        coVerify(exactly = 1) { dao.insertCenter(c1) }
        coVerify(exactly = 1) { dao.insertCenter(c2) }
    }
}