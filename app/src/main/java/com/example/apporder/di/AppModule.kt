package com.example.apporder.di

import android.content.Context
import androidx.room.Room
import com.example.apporder.room.AppDao
import com.example.apporder.room.AppDatabase
import com.example.apporder.room.repository.FileDataRepository
import com.example.apporder.room.repository.FileDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDao(appDatabase: AppDatabase): AppDao {
        return appDatabase.dao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }


    @Singleton
    @Provides
    fun provideFileDataRepositoryImpl(dao: AppDao): FileDataRepository {
        return FileDataRepositoryImpl(dao)
    }

}