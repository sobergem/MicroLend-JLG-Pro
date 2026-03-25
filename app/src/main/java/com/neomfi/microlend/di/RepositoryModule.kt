package com.neomfi.microlend.di

import com.neomfi.microlend.data.repository.JlgGroupRepositoryImpl
import com.neomfi.microlend.data.repository.LeadRepositoryImpl
import com.neomfi.microlend.data.repository.VillageCenterRepositoryImpl
import com.neomfi.microlend.domain.repository.JlgGroupRepository
import com.neomfi.microlend.domain.repository.LeadRepository
import com.neomfi.microlend.domain.repository.VillageCenterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLeadRepository(leadRepositoryImpl: LeadRepositoryImpl): LeadRepository

    @Binds
    @Singleton
    abstract fun bindJlgGroupRepository(jlgGroupRepositoryImpl: JlgGroupRepositoryImpl): JlgGroupRepository

    @Binds
    @Singleton
    abstract fun bindVillageCenterRepository(villageCenterRepositoryImpl: VillageCenterRepositoryImpl): VillageCenterRepository
}