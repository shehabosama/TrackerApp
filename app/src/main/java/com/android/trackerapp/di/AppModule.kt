package com.android.trackerapp.di

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.android.trackerapp.R
import com.android.trackerapp.db.RunningDatabase
import com.android.trackerapp.other.Constants
import com.android.trackerapp.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.android.trackerapp.other.Constants.KEY_NAME
import com.android.trackerapp.other.Constants.KEY_WEIGHT
import com.android.trackerapp.other.Constants.RUNNING_DATABASE_NAME
import com.android.trackerapp.other.Constants.SHARE_PREFERENCES
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

    @Provides
    fun provideSharePreferences(@ApplicationContext app :Context) = app.getSharedPreferences(
        SHARE_PREFERENCES,MODE_PRIVATE)

    @Provides
    fun provideName(sharedPreferences: SharedPreferences) = sharedPreferences.getString(KEY_NAME,"")?:""


    @Provides
    fun provideWeight(sharedPreferences: SharedPreferences) = sharedPreferences.getFloat(KEY_WEIGHT,80f)

    @Provides
    fun provideFirstTimeToggle(sharedPreferences: SharedPreferences) = sharedPreferences.getBoolean(
        KEY_FIRST_TIME_TOGGLE,true)
}
