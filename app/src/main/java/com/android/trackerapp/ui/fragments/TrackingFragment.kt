package com.android.trackerapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.trackerapp.R
import com.android.trackerapp.db.Run
import com.android.trackerapp.other.Constants.ACTION_PAUSE_SERVICE
import com.android.trackerapp.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.android.trackerapp.other.Constants.ACTION_STOP_SERVICE
import com.android.trackerapp.other.Constants.MAP_ZOOM
import com.android.trackerapp.other.Constants.POLYLINE_COLOR
import com.android.trackerapp.other.Constants.POLYLINE_WIDTH
import com.android.trackerapp.other.TrackingUtility
import com.android.trackerapp.services.Polyline
import com.android.trackerapp.services.TrackingService
import com.android.trackerapp.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_traking.*
import java.util.*
import javax.inject.Inject
import kotlin.math.round
const val CANCEL_TRACKING_DIALOG_TAG = "CancelDialog"
@AndroidEntryPoint
class TrackingFragment :Fragment(R.layout.fragment_traking) {
    private val viewModel: MainViewModel by viewModels()
    private var menu:Menu? = null
    private var map :GoogleMap?=null
    private var  isTraacking = false
    private var currentTimeMillis = 0L
    @set:Inject
    var wight = 80f
    private var pathPoint = mutableListOf<Polyline>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        btnToggleRun.setOnClickListener {
           toggleRun()
        }
        if(savedInstanceState != null){
            val cancelTrackingDialog = parentFragmentManager.findFragmentByTag(
                CANCEL_TRACKING_DIALOG_TAG) as CancelTrackingDialog?
            cancelTrackingDialog?.setYesListener {
                stopRun()
            }
        }
        btnFinishRun.setOnClickListener {
            zoomToSeeWholeTrack()
            endRunAndSaveToDb()
        }
        mapView.getMapAsync {
            map = it
            addAllPolylines()
        }
        subscribeToObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)

    }
    private fun moveCameraToUser(){
        if(pathPoint.isNotEmpty() && pathPoint.last().isNotEmpty()){
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pathPoint.last().last(), MAP_ZOOM))
        }
    }

    private fun zoomToSeeWholeTrack(){
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoint){
            for(pos in polyline){
                bounds.include(pos)
            }
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.05f).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb(){
        map?.snapshot {bmp->
            var distanceInMeters = 0
            for(polyline in pathPoint){
                distanceInMeters+=TrackingUtility.calculatePolylineLength(polyline).toInt()
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (currentTimeMillis/1000f/60/60) *10 )/10f
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters /1000f) * wight).toInt()

            val run = Run(bmp,dateTimestamp,avgSpeed,distanceInMeters,currentTimeMillis,caloriesBurned)
            viewModel.insertRun(run)
            Snackbar.make(
                requireActivity().findViewById(R.id.rootView),
                "Run saved successfully",
                Snackbar.LENGTH_LONG
            ).show()
            stopRun()
        }
    }

    private fun updateTracking(isTracking:Boolean){
        this.isTraacking = isTracking
        if(!isTracking && currentTimeMillis > 0L) {
            btnToggleRun.text = "Start"
            btnFinishRun.visibility = View.VISIBLE
        } else if(isTracking) {
            btnToggleRun.text = "Stop"
            btnFinishRun.visibility = View.GONE
            menu?.getItem(0)?.isVisible = true

        }

    }

    private fun subscribeToObservers(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoint = it
            addLatestPolyline()
            moveCameraToUser()
        })
        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeMillis = it
             val formattedTime = TrackingUtility.getFormattedStopWatchTime(currentTimeMillis,true)
            tvTimer.text = formattedTime
        })
    }
    private fun toggleRun(){

        if(isTraacking){
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu,menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cancel_tracking->{
                showCancelTrackingDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(currentTimeMillis>0L){
            this.menu?.getItem(0)?.isVisible = true
        }
    }
    private fun showCancelTrackingDialog(){
        CancelTrackingDialog().apply {
            setYesListener {
                stopRun()
            }
        }.show(parentFragmentManager, CANCEL_TRACKING_DIALOG_TAG)
    }
    private fun stopRun(){
        tvTimer.text = "00:00:00:00"
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }
    private fun  addAllPolylines(){
        for(polyline in pathPoint){
            val polylineOptions = PolylineOptions().color(POLYLINE_COLOR).width(POLYLINE_WIDTH).addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }
    private fun addLatestPolyline(){
        if(pathPoint.isNotEmpty()&& pathPoint.last().size>1){
            val preLastLatLong = pathPoint.last()[pathPoint.last().size - 2]
            val lastLatLong = pathPoint.last().last()
            val polylineOptions = PolylineOptions().color(POLYLINE_COLOR).width(POLYLINE_WIDTH).add(preLastLatLong).add(lastLatLong)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(action:String) = Intent(requireContext(), TrackingService::class.java).also {
        it.action = action
        requireContext().startService(it)
    }

    private fun sendCommand(action:String){
        var intent = Intent(requireContext(),TrackingService::class.java)
        intent.action = action
        requireContext().startService(intent)
    }
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}