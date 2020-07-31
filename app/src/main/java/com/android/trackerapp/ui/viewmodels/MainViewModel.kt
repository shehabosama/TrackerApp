package com.android.trackerapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.android.trackerapp.repositories.MainRepository
import javax.inject.Inject


class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
):ViewModel(){

}