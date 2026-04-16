package com.neomfi.microlend.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LeadDto(
    @SerializedName("lead_id") val leadId: String,
    @SerializedName("group_Id") val groupId: String?,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("aadhaar_no") val aadhaarNo: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("monthly_income") val monthlyIncome: Double,
    @SerializedName("monthly_expenses") val monthlyExpenses: Double,
    @SerializedName("sync_status") val syncStatus: String,
    @SerializedName("assigned_status") val assignedStatus: String
)
