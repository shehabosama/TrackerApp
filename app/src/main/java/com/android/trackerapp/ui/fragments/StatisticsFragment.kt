package com.android.trackerapp.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.trackerapp.R
import com.android.trackerapp.ui.viewmodels.MainViewModel
import com.android.trackerapp.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment :Fragment(R.layout.fragment_statistics) {
    private val viewModel: StatisticsViewModel by viewModels()
}