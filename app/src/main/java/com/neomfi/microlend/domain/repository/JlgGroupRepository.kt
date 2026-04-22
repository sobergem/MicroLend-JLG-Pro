package com.neomfi.microlend.domain.repository


import com.neomfi.microlend.domain.model.JlgGroup
import kotlinx.coroutines.flow.Flow

interface JlgGroupRepository {
    fun getGroupsByCenter(centerId: String): Flow<List<JlgGroup>>
    suspend fun insertGroup(group: JlgGroup)
    suspend fun deleteGroup(group: JlgGroup)

    fun hasUnSyncedGroup(): Flow<Boolean>

    suspend fun createGroupWithLeads(group: JlgGroup, leads: List<String>)
}