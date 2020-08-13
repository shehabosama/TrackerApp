package com.android.trackerapp.other

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import com.android.trackerapp.services.Polyline
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import java.util.concurrent.TimeUnit

object TrackingUtility {
    fun hasLocationPermission(context : Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        }else{
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )

        }

    fun calculatePolylineLength(polyline: Polyline):Float{
        var distance = 0f
        for(i in 0..polyline.size -2){
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance +=result[0]
        }
        return distance
    }

    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if(!includeMillis) {
            return "${if(hours < 10) "0" else ""}$hours:" +
                    "${if(minutes < 10) "0" else ""}$minutes:" +
                    "${if(seconds < 10) "0" else ""}$seconds"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds:" +
                "${if(milliseconds < 10) "0" else ""}$milliseconds"
    }


    var NUMBER = 0L
    var minutes = 0L
    var seconds = 0L
    var formattedNumber = ""
    fun format(number:Long?) : String{
        number?.let {
            minutes = (number%3600)/60
            seconds = number%60

        }
        formattedNumber = String.format(
            Locale.getDefault(),
            "%02d:%02d",
            minutes,
            seconds
        )

        return formattedNumber
    }

    fun getTime() : Long{
        return NUMBER
    }


}