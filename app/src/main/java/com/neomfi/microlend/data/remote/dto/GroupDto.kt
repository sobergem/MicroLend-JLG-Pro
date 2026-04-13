package com.neomfi.microlend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("group_id") val groupId: String,
    @SerializedName("center_id") val centerId: String,
    @SerializedName("group_name") val groupName: String,
    @SerializedName("is_complete") val isComplete: Boolean
)
