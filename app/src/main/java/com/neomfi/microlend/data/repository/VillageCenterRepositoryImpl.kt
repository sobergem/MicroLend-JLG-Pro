package com.neomfi.microlend.data.repository

import com.neomfi.microlend.data.dao.VillageCenterDao
import com.neomfi.microlend.data.local.entity.VillageCenterEntity
import com.neomfi.microlend.data.remote.api.MicroLendApi
import com.neomfi.microlend.data.remote.mapper.toDomain
import com.neomfi.microlend.data.remote.mapper.toEntity
import com.neomfi.microlend.domain.model.VillageCenter
import com.neomfi.microlend.domain.repository.VillageCenterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VillageCenterRepositoryImpl @Inject constructor(
    private val dao: VillageCenterDao,
    private val api: MicroLendApi
) : VillageCenterRepository{
    override fun getAllCenters(): Flow<List<VillageCenter>>{
        return dao.getAllCenters().map{entityList ->
            entityList.map{it.toDomain()}
        }
    }

    override suspend fun insertCenter(centers:List<VillageCenterEntity>) = dao.insertCenters(centers)
    override suspend fun fetchAndCacheCenters(): Result<Unit> {
        return try{
            val response = api.getAllCenters()
            if(response.isSuccessful){
                val networkCenters = response.body()?:emptyList()
                val centers = networkCenters.map { it.toEntity() }
                dao.clearAllCenters()
                dao.insertCenters(centers)
                Result.success(Unit)
            }else{
                Result.failure(Exception("Failed to fetch centers: ${response.code()}"))
            }
        }catch(e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }

}