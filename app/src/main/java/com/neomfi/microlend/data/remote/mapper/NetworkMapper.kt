package com.neomfi.microlend.data.remote.mapper

import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.VillageCenterEntity
import com.neomfi.microlend.data.remote.dto.CenterDto
import com.neomfi.microlend.data.remote.dto.GroupDto
import com.neomfi.microlend.data.remote.dto.LeadDto
import com.neomfi.microlend.domain.model.VillageCenter

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
fun CenterDto.toEntity(): VillageCenterEntity{
    return VillageCenterEntity(
        id = this.centerId,
        name = this.centerName,
        villageName = this.villageName,
        meetingDay = this.meetingDay,
        pinCode = null
    )
}

fun VillageCenterEntity.toDomain(): VillageCenter{
    return VillageCenter(
        id = this.id,
        name = this.name,
        villageName = this.villageName,
        meetingDay = this.meetingDay,
        pinCode = this.pinCode
    )
}
