package com.neomfi.microlend.data.remote.mapper

import com.neomfi.microlend.data.local.entity.JlgGroupEntity
import com.neomfi.microlend.data.local.entity.LeadEntity
import com.neomfi.microlend.data.local.entity.VillageCenterEntity
import com.neomfi.microlend.data.remote.dto.CenterDto
import com.neomfi.microlend.data.remote.dto.GroupDto
import com.neomfi.microlend.data.remote.dto.LeadDto
import com.neomfi.microlend.domain.model.JlgGroup
import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.model.VillageCenter

fun LeadEntity.toDto(): LeadDto{
    return LeadDto(
        leadId = this.id,
        groupId = this.groupID,
        fullName = this.name,
        aadhaarNo = this.aadhaarNumber,
        syncStatus = this.syncStatus,
        phone = this.phone,
        monthlyIncome = this.monthlyIncome,
        monthlyExpenses = this.monthlyExpenses,
        assignedStatus = this.assignmentStatus
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

fun Lead.toEntity():LeadEntity{
    return LeadEntity(
        id = this.id,
        centerID = this.centerId,
        groupID = this.groupId,
        name = this.name,
        aadhaarNumber = this.aadhaarNumber,
        phone = this.phone,
        monthlyIncome = this.monthlyIncome,
        monthlyExpenses = this.monthlyExpenses,
        syncStatus = this.syncStatus.toDbString(),
        assignmentStatus = this.assignmentStatus.toDbString()
    )
}

fun LeadEntity.toDomain():Lead{
    return Lead(
        id = this.id,
        centerId = this.centerID,
        groupId = this.groupID,
        name = this.name,
        aadhaarNumber = this.aadhaarNumber,
        phone = this.phone,
        monthlyIncome = this.monthlyIncome,
        monthlyExpenses = this.monthlyExpenses,
        syncStatus = this.syncStatus.toSyncStatus(),
        assignmentStatus = this.assignmentStatus.toAssignmentStatus()
    )
}

fun Lead.toNetworkRequest(): LeadDto{
    return LeadDto(
        leadId = this.id,
        groupId = this.groupId,
        fullName = this.name,
        aadhaarNo = this.aadhaarNumber,
        syncStatus = this.syncStatus.name,
        phone = this.phone,
        monthlyIncome = this.monthlyIncome,
        monthlyExpenses = this.monthlyExpenses,
        assignedStatus = this.assignmentStatus.name
    )
}

fun JlgGroup.toEntity(): JlgGroupEntity{
    return JlgGroupEntity(
        id = this.id,
        centerID = this.centerID,
        name = this.name,
        syncStatus = this.syncStatus.toDbString()
    )
}

fun JlgGroupEntity.toDomain(): JlgGroup{
    return JlgGroup(
        id = this.id,
        centerID = this.centerID,
        name = this.name,
        syncStatus = this.syncStatus.toSyncStatus()
    )
}
