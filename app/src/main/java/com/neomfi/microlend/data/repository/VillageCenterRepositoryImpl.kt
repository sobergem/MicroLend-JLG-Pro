package com.neomfi.microlend.data.repository

import com.neomfi.microlend.data.dao.VillageCenterDao
import com.neomfi.microlend.data.local.entity.VillageCenterEntity
import com.neomfi.microlend.domain.repository.VillageCenterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VillageCenterRepositoryImpl @Inject constructor(
    private val dao: VillageCenterDao
) : VillageCenterRepository{
    override fun getAllCenters(): Flow<List<VillageCenterEntity>> = dao.getAllCenters()

    override suspend fun insertCenter(center: VillageCenterEntity) = dao.insertCenter(center)

}