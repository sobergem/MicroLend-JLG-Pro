package com.neomfi.microlend.data.remote.api

import com.neomfi.microlend.data.remote.dto.BulkSyncRequest
import com.neomfi.microlend.data.remote.dto.CenterDto
import com.neomfi.microlend.data.remote.dto.CreateLeadResponseDto
import com.neomfi.microlend.data.remote.dto.LeadDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MicroLendApi {
    @POST("sync/bulk")
    suspend fun syncBulkData(@Body request: BulkSyncRequest): Response<Unit>

    @GET("centers")
    suspend fun getAllCenters(): Response<List<CenterDto>>

    @POST("leads")
    suspend fun createSingleLead(@Body request: LeadDto): Response<CreateLeadResponseDto>
}