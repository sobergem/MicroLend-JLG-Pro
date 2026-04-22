package com.neomfi.microlend.data.repository

import androidx.room.withTransaction
import com.neomfi.microlend.data.MicroLendDatabase
import com.neomfi.microlend.data.dao.JlgGroupDao
import com.neomfi.microlend.data.dao.LeadDao
import com.neomfi.microlend.data.remote.mapper.toDbString
import com.neomfi.microlend.data.remote.mapper.toDomain
import com.neomfi.microlend.data.remote.mapper.toEntity
import com.neomfi.microlend.domain.model.AssignmentStatus
import com.neomfi.microlend.domain.model.JlgGroup
import com.neomfi.microlend.domain.repository.JlgGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JlgGroupRepositoryImpl @Inject constructor(
    private val dao: JlgGroupDao,
    private val leadDao: LeadDao,
    private val database: MicroLendDatabase
    ) : JlgGroupRepository {
    override fun getGroupsByCenter(centerId: String): Flow<List<JlgGroup>> = dao.getGroupsByCenter(centerId).map { groups -> groups.map{it.toDomain()} }
    override suspend fun insertGroup(group: JlgGroup) = dao.insertGroup(group.toEntity())

    override suspend fun deleteGroup(group: JlgGroup) = dao.deleteGroup(group.toEntity())
    override fun hasUnSyncedGroup(): Flow<Boolean> = dao.hasUnSyncedGroups()
    override suspend fun createGroupWithLeads(
        group: JlgGroup,
        leads: List<String>
    ) {
        val groupEntity = group.toEntity()
        database.withTransaction {
            dao.insertGroup(groupEntity)
            if(leads.isNotEmpty()){
                leadDao.assignLeadsToGroup(leads, group.id, AssignmentStatus.ASSIGNED.toDbString())
            }
        }
    }
}