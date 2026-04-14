package com.neomfi.microlend.data.remote.mapper

import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.remote.dto.GroupDto
import com.neomfi.microlend.data.remote.dto.LeadDto

fun LeadEntity.toDto(): LeadDto{
    return LeadDto(
        leadId = this.id,
        groupId = this.groupID,
        fullName = this.name,
        aadhaarNo = this.aadhaarNumber,
        syncStatus = this.syncStatus.name
    )
}

fun JlgGroupEntity.toDto(): GroupDto{
    return GroupDto(
        groupId = this.id,
        centerId = this.centerID,
        groupName = this.name,
        isComplete = true
    )
}
