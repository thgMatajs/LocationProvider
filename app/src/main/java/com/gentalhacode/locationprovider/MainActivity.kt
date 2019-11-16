package com.gentalhacode.locationprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.gentalhacode.location_provider.gps.GpsStateListener
import com.gentalhacode.location_provider.gps.GpsStatus
import com.gentalhacode.location_provider.location.LocationLiveData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkGpsState()
    }


    private fun checkGpsState() {
        GpsStateListener(this).observe(this, Observer {
            when(it) {
                GpsStatus.Enabled() -> {
                    toast("GPS ATIVO")
                    getCurrentLocation()
                }
                GpsStatus.Disabled() -> toast("GPS DESATIVADO")
                GpsStatus.Failed() -> toast("GPS ERRO")
            }
        })
    }

    private fun getCurrentLocation() {
        LocationLiveData(this).observe(this, Observer {
            txtLatLong.text = "lat::${it.latitude}\nlong::${it.longitude}"
        })
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
