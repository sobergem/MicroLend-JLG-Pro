package com.neomfi.microlend.domain.model

data class VillageCenter(
    val id: String,
    val name: String,
    val villageName: String,
    val meetingDay: String,
    val pinCode: String? = null
)