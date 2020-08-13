package com.android.trackerapp.di

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.android.trackerapp.R
import com.android.trackerapp.db.RunningDatabase
import com.android.trackerapp.other.Constants
import com.android.trackerapp.other.Constants.RUNNING_DATABASE_NAME
import com.android.trackerapp.ui.MainActivity
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