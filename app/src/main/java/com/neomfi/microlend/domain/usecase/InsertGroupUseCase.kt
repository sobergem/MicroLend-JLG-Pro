package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.domain.model.JlgGroup
import com.neomfi.microlend.domain.repository.JlgGroupRepository
import javax.inject.Inject

class InsertGroupUseCase @Inject constructor(
    private val repository: JlgGroupRepository
) {
    suspend operator fun invoke(group: JlgGroup){
        repository.insertGroup(group)
    }
}