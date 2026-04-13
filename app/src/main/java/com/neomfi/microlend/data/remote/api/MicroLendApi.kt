package com.neomfi.microlend.data.remote.api

import com.neomfi.microlend.data.remote.dto.BulkSyncRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MicroLendApi {
    @POST("sync/bulk")
    suspend fun syncBulkData(@Body request: BulkSyncRequest): Response<Unit>
}