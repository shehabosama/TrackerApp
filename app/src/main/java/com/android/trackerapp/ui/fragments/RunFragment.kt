package com.android.trackerapp.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.trackerapp.R
import com.android.trackerapp.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.android.trackerapp.other.TrackingUtility
import com.android.trackerapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.fragment_setup.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment :Fragment(R.layout.fragment_run) , EasyPermissions.PermissionCallbacks{
    private val viewModel:MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()
        fab.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        })

    }

    private fun requestPermission(){
        if(TrackingUtility.hasLocationPermission(requireContext())){
            return
        }
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions( this,
                "You need to accept location permission to user this app .",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
      if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
          AppSettingsDialog.Builder(this).build().show()
      }else{
          requestPermission()
      }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}