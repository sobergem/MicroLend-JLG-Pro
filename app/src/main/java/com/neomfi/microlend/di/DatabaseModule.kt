package com.neomfi.microlend.di

import android.content.Context
import androidx.room.Room
import com.neomfi.microlend.data.MicroLendDatabase
import com.neomfi.microlend.data.dao.JlgGroupDao
import com.neomfi.microlend.data.dao.LeadDao
import com.neomfi.microlend.data.dao.VillageCenterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMicroLendDatabase(
        @ApplicationContext context: Context
    ): MicroLendDatabase {
        return Room.databaseBuilder(
            context,
            MicroLendDatabase::class.java,
            "microlend_local_db"
        ).fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideLeadDao(database: MicroLendDatabase): LeadDao{
        return database.leadDao()
    }

    @Provides
    @Singleton
    fun provideJlgGroupDao(database: MicroLendDatabase): JlgGroupDao{
        return database.groupDao()
    }

    @Provides
    @Singleton
    fun provideVillageCenterDao(database: MicroLendDatabase): VillageCenterDao{
        return database.centerDao()
    }
}