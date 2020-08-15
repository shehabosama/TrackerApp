package com.android.trackerapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.trackerapp.R
import com.android.trackerapp.other.Constants.KEY_NAME
import com.android.trackerapp.other.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_setup.*
import kotlinx.android.synthetic.main.fragment_setup.etName
import kotlinx.android.synthetic.main.fragment_setup.etWeight
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment :Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFieldsFromSharePref()
        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharePreferences()
            if(success){
                Snackbar.make(view , "Saved changed",Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(view , "Please fill out all fields",Snackbar.LENGTH_LONG).show()

            }
        }
    }
    private fun loadFieldsFromSharePref(){
        val name  = sharedPreferences.getString(KEY_NAME , "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT , 80f)
        etName.setText(name)
        etWeight.setText(weight.toString())
    }
    private fun applyChangesToSharePreferences():Boolean{
        val nameText = etName.text.toString()
        val weightText = etWeight.text.toString()
        if(nameText.isEmpty() || weightText.isEmpty()){
            return false
        }
        sharedPreferences.edit().putString(KEY_NAME,nameText).putFloat(KEY_WEIGHT , weightText.toFloat()).apply()
        val toolbarText = "let's go $nameText"
        requireActivity().tvToolbarTitle.text = toolbarText
        return true
    }
}