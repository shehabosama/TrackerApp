package com.android.trackerapp.repositories

import com.android.trackerapp.db.Run
import com.android.trackerapp.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao : RunDAO
){
    suspend fun insertRun (run : Run) = runDao.insertRun(run)
    suspend fun deleteRun(run : Run) = runDao.deleteRun(run)
    fun getAllRunSortedByDate() = runDao.getAllRunSortedByDate()
    fun getAllRunSortedByDistance() = runDao.getAllRunSortedByDistance()
    fun getAllRunSortedByTimeMilliSecond() = runDao.getAllRunSortedByTimeMilliSecond()
    fun getAllRunSortedByAvgSpeed() = runDao.getAllRunSortedByAvgSpeed()
    fun getAllRunSortedByCaloriesBurned() = runDao.getAllRunSortedByCaloriesBurned()

    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()
    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()
    fun getTotalTimeMillis() = runDao.getTotalTimeInMillis()
    fun getTotalDistance() = runDao.getTotalDistance()


}