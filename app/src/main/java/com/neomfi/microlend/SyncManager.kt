package com.neomfi.microlend

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.neomfi.microlend.data.worker.SyncWorker
import java.util.concurrent.TimeUnit

object SyncManager {
    fun scheduleBackGroundSync(context: Context){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10,
                TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "OfflineSync",
            androidx.work.ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }
}