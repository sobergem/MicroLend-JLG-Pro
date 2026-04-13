package com.neomfi.microlend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BulkSyncRequest(
    @SerializedName("groups") val groups: List<GroupDto>,
    @SerializedName("leads") val leads: List<LeadDto>
)
