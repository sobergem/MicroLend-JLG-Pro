package com.neomfi.microlend.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.neomfi.microlend.domain.repository.SyncRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams : WorkerParameters,
    private val syncRepository: SyncRepository) : CoroutineWorker(appContext, workerParams){
    override suspend fun doWork(): Result {
        return try{
            val success = syncRepository.performBulkSync()
            if(success){
                Result.success()
            }else{
                Result.retry()
            }
        }catch(e: Exception){
            Result.retry()
        }
    }

}
