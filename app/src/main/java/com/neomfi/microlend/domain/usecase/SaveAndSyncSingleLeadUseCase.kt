package com.neomfi.microlend.domain.usecase

import com.neomfi.microlend.data.remote.api.MicroLendApi
import com.neomfi.microlend.data.remote.mapper.toNetworkRequest
import com.neomfi.microlend.domain.model.Lead
import com.neomfi.microlend.domain.repository.LeadRepository
import javax.inject.Inject

class SaveAndSyncSingleLeadUseCase @Inject constructor(
    private val leadRepository: LeadRepository,
    private val api: MicroLendApi
) {
    suspend operator fun invoke(lead: Lead): Result<Unit>{
        return try{
            leadRepository.insertLead(lead)

            val request = lead.toNetworkRequest()
            val response = api.createSingleLead(request)
            if(response.isSuccessful  && response.body()?.status == "success"){
                leadRepository.updateSyncStatus(listOf(lead.id), "SYNCED")
            }
            Result.success(Unit)
        }catch(e : Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }
}