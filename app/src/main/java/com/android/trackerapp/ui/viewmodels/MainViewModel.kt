package com.android.trackerapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.trackerapp.db.Run
import com.android.trackerapp.other.SortType
import com.android.trackerapp.repositories.MainRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
):ViewModel(){

    private  val runsSortedByDate = mainRepository.getAllRunSortedByDate()
    private val runsSortedByDistance = mainRepository.getAllRunSortedByDistance()
    private val runsSortedByCaloriesBurned = mainRepository.getAllRunSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = mainRepository.getAllRunSortedByTimeMilliSecond()
    private val runsSortedByAvgSpeed = mainRepository.getAllRunSortedByAvgSpeed()

    val runs = MediatorLiveData<List<Run>>()
    var sortType = SortType.DATE
    init {
        runs.addSource(runsSortedByDate){result->
            if(sortType == SortType.DATE){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByAvgSpeed){result->
            if(sortType == SortType.ABG_SPEED){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDistance){result->
            if(sortType == SortType.DISTANCE){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCaloriesBurned){result->
            if(sortType == SortType.CALORIES_BURNED){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis){result->
            if(sortType == SortType.RUNNING_TIME){
                result?.let { runs.value = it }
            }
        }
    }

    public  fun sortRuns(sortType: SortType) = when(sortType){
        SortType.DATE->runsSortedByDate.value?.let { runs.value=it }
        SortType.RUNNING_TIME->runsSortedByTimeInMillis.value?.let { runs.value=it }
        SortType.ABG_SPEED->runsSortedByAvgSpeed.value?.let { runs.value=it }
        SortType.DISTANCE->runsSortedByDistance.value?.let { runs.value=it }
        SortType.CALORIES_BURNED->runsSortedByCaloriesBurned.value?.let { runs.value=it }
    }.also {
        this.sortType = sortType
    }
    fun insertRun(run:Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }


}