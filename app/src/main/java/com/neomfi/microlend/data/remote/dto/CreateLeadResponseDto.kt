package com.neomfi.microlend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateLeadResponseDto(
    val status : String,
    @SerializedName("lead_id") val leadId: String
)