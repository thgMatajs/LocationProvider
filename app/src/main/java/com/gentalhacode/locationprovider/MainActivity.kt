package com.gentalhacode.locationprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.gentalhacode.location_provider.geo_coder.GetLatLong
import com.gentalhacode.location_provider.geo_coder.PlaceLiveData
import com.gentalhacode.location_provider.gps.GpsStateListener
import com.gentalhacode.location_provider.gps.GpsStatus
import com.gentalhacode.location_provider.location.LocationLiveData
import com.gentalhacode.location_provider.logger.loggerSuccess
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        checkGpsState()

        btnSearch.setOnClickListener {
            if (!edtSearch.text.isNullOrBlank()) {
             txtLatLong.text = GetLatLong.byPlace(this, edtSearch.text.toString()).toString()
            }
        }
    }


    private fun checkGpsState() {
        GpsStateListener(this).observe(this, Observer {
            when (it) {
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
            toast("lat::${it.latitude}/long::${it.longitude}")
            PlaceLiveData(this, it.latitude, it.longitude).observe(this, Observer { place ->
                txtPlace.text = place.fullAddress
            })
        })
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
