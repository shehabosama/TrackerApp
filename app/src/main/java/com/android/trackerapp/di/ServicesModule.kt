package com.android.trackerapp.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.android.trackerapp.R
import com.android.trackerapp.other.Constants
import com.android.trackerapp.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServicesModule {
    @ServiceScoped
    @Provides
    fun providesNotificationBuilder(@ApplicationContext app: Context) = NotificationCompat.Builder(app,
        Constants.NOTIFICATION_CHANNEL_ID
    ).setAutoCancel(false).setOngoing(true).setSmallIcon(
        R.drawable.ic_run).setContentTitle("Running app").setContentIntent(providesPendingIntent(app))
    @ServiceScoped
    @Provides
    fun providesPendingIntent(@ApplicationContext app : Context)  = PendingIntent.getActivity(
        app,
        0,
        Intent(
            app, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
        }, PendingIntent.FLAG_UPDATE_CURRENT
    )
    @ServiceScoped
    @Provides
    fun providesFusedLocationProviderClient(@ApplicationContext app:Context) = FusedLocationProviderClient(app)
}