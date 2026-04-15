package com.neomfi.microlend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CenterDto(
    @SerializedName("center_id") val centerId:String,
    @SerializedName("center_name") val centerName: String,
    @SerializedName("village_name") val villageName: String,
    @SerializedName("meeting_day") val meetingDay:String
)
