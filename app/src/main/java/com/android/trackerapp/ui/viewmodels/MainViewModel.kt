package com.android.trackerapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.trackerapp.db.Run
import com.android.trackerapp.repositories.MainRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
):ViewModel(){

    fun insertRun(run:Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }

}