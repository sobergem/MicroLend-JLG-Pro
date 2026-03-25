package com.neomfi.microlend.domain.repository

import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import kotlinx.coroutines.flow.Flow

interface JlgGroupRepository {
    fun getGroupsByCenter(centerId: String): Flow<List<JlgGroupEntity>>
    suspend fun insertGroup(group: JlgGroupEntity)
    suspend fun deleteGroup(group: JlgGroupEntity)
}