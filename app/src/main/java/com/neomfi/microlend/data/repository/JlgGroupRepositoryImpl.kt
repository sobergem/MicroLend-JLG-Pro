package com.neomfi.microlend.data.repository

import com.neomfi.microlend.data.dao.JlgGroupDao
import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.domain.repository.JlgGroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JlgGroupRepositoryImpl @Inject constructor(
    private val dao: JlgGroupDao
) : JlgGroupRepository {
    override fun getGroupsByCenter(centerId: String): Flow<List<JlgGroupEntity>> = dao.getGroupsByCenter(centerId)
    override suspend fun insertGroup(group: JlgGroupEntity) = dao.insertGroup(group)

    override suspend fun deleteGroup(group: JlgGroupEntity) = dao.deleteGroup(group)
}