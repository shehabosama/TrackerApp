package com.android.trackerapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.trackerapp.R
import com.android.trackerapp.other.TrackingUtility
import com.android.trackerapp.ui.viewmodels.MainViewModel
import com.android.trackerapp.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment :Fragment(R.layout.fragment_statistics) {
    private val viewModel: StatisticsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         subscribeToObserver()
    }
    private fun subscribeToObserver(){
        viewModel.totalTimeRun.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                tvTotalTime.text = totalTimeRun
            }
        })
        viewModel.totalDistance.observe(viewLifecycleOwner , Observer {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km *10f)/10f
                tvTotalDistance.text = "$totalDistance km"

            }
        })
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                val avgSpeed = round(it*10f)/10f
                tvAverageSpeed.text = "$avgSpeed km/h"
            }
        })

        viewModel.totalCaloriesSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalCalories = "${it} kcal"
                tvTotalCalories.text = totalCalories
            }
        })
    }
}