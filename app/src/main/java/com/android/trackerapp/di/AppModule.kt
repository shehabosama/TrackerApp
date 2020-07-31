package com.android.trackerapp.di

import android.content.Context
import androidx.room.Room
import com.android.trackerapp.db.RunningDatabase
import com.android.trackerapp.other.Constants.RUNNING_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()

   // @Singleton
    @Provides
    fun provideRunDao(db : RunningDatabase) = db.getRunDao()
}