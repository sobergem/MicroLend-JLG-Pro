package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.domain.repository.JlgGroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupsByCenterUseCase @Inject constructor(
    private val repository: JlgGroupRepository
){
    operator fun invoke(centerId: String): Flow<List<JlgGroupEntity>>{
        return repository.getGroupsByCenter(centerId)
    }
}