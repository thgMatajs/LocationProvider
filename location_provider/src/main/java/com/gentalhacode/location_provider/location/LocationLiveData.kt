package com.gentalhacode.location_provider.location

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.gentalhacode.location_provider.model.CurrentLocation
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

/**
 * .:.:.:. Created by @thgMatajs on 16/11/19 .:.:.:.
 */
class LocationLiveData(
    context: Context
) : LiveData<CurrentLocation>() {

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onActive() {
        super.onActive()
        fusedLocationClient
            .lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }

    private fun setLocationData(location: Location) {
        value = CurrentLocation(
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.let {
                setLocationData(it.locations.first())
                println("GENTALHA_LOG:::Last Location>>>${it.lastLocation}")
                println("GENTALHA_LOG:::Locations>>>${it.locations}")
            } ?: return
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }
}