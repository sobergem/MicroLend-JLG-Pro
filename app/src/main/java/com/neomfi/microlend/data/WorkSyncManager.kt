package com.neomfi.microlend.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.neomfi.microlend.domain.SyncManager
import com.neomfi.microlend.worker.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkSyncManager @Inject constructor(
    @ApplicationContext private val context: Context
): SyncManager {
    private val workManager = WorkManager.getInstance(context)
    override fun enqueueManualSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork("BULK_SYNC_WORK", androidx.work.ExistingWorkPolicy.KEEP, request)
    }

    override fun observeSyncState(): Flow<WorkInfo.State?> {
        return workManager.getWorkInfosForUniqueWorkFlow("BULK_SYNC_WORK").map{it.firstOrNull()?.state}
    }

}