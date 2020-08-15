package com.android.trackerapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.android.trackerapp.R
import com.android.trackerapp.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.android.trackerapp.other.Constants.KEY_NAME
import com.android.trackerapp.other.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import timber.log.Timber
import java.util.prefs.AbstractPreferences
import javax.inject.Inject
@AndroidEntryPoint
class SetupFragment :Fragment(R.layout.fragment_setup) {

    @Inject lateinit var sharePreferences: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isFirstAppOpen) {
           Timber.d("onViewCreated: check if this first time")
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true).build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        tvContinue.setOnClickListener(View.OnClickListener {
            val success = writePersonalDataToSharePref()
            if(success){
                Timber.d("setOnClickListener: check if this first time")

                findNavController().navigate(R.id.action_setupFragment_to_runFragment)

            }else{
                Snackbar.make(requireView() , "Please enter all the fields", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun writePersonalDataToSharePref():Boolean{
        val name = etName.text.toString()
        val weight = etWeight.text.toString()
        if(name.isEmpty()||weight.isEmpty()){
            return false
        }
        sharePreferences.edit().putString(KEY_NAME,name).putFloat(KEY_WEIGHT,weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE,false).apply()
        val toolbarText = "Let's go, $name!"
        requireActivity().tvToolbarTitle.text  =toolbarText
        return true

    }

    companion object {
        const val TAG = "SetupFragment"
    }
}