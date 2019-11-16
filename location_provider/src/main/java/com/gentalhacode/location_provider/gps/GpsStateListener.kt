package com.gentalhacode.location_provider.gps

import android.app.Activity
import android.content.*
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import com.gentalhacode.location_provider.location.LocationLiveData
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient

/**
 * .:.:.:. Created by @thgMatajs on 16/11/19 .:.:.:.
 */
class GpsStateListener(
    private val context: Context
) : LiveData<GpsStatus>() {

    private val settingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val locationSettingsRequest: LocationSettingsRequest
    private val gpsSwitchStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }

    init {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(LocationLiveData.locationRequest)
        locationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
    }

    override fun onInactive() = unregisterReceiver()

    override fun onActive() {
        registerReceiver()
        checkGpsAndReact()
    }

    private fun checkGpsAndReact() {
        if (isLocationEnabled()) {
            postValue(GpsStatus.Enabled())
        } else {
            postValue(GpsStatus.Disabled())
            settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(context as Activity) {
                    postValue(GpsStatus.Enabled())
                }
                .addOnFailureListener(context) { exception ->
                    when ((exception as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = exception as ResolvableApiException
                                rae.startResolutionForResult(context, GPS_REQUEST)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.i(ContentValues.TAG, "PendingIntent unable to execute request.")
                                postValue(GpsStatus.Failed())
                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                            Log.e(ContentValues.TAG, errorMessage)
                            postValue(GpsStatus.Failed())
                        }
                    }
                }
        }
    }

    private fun isLocationEnabled() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(LocationManager::class.java)!!
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
    } else {

        try {
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE
            ) != Settings.Secure.LOCATION_MODE_OFF
        } catch (e: Settings.SettingNotFoundException) {
            false
        }
    }

    private fun unregisterReceiver() = context.unregisterReceiver(gpsSwitchStateReceiver)

    private fun registerReceiver() = context.registerReceiver(
        gpsSwitchStateReceiver, IntentFilter(
            LocationManager.PROVIDERS_CHANGED_ACTION
        )
    )

    companion object {
        const val LOCATION_REQUEST = 100
        const val GPS_REQUEST = 101
    }
}