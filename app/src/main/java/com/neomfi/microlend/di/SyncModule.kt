package com.neomfi.microlend.di

import com.neomfi.microlend.data.WorkSyncManager
import com.neomfi.microlend.data.repository.SyncRepositoryImpl
import com.neomfi.microlend.domain.SyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {
    @Binds
    @Singleton
    abstract fun bindSyncManager(syncManager: WorkSyncManager): SyncManager
}