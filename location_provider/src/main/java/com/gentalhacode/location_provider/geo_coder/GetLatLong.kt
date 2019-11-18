package com.gentalhacode.location_provider.geo_coder

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.gentalhacode.location_provider.logger.loggerError
import com.gentalhacode.location_provider.logger.loggerSuccess
import com.gentalhacode.location_provider.model.CurrentLocation
import java.lang.Exception

/**
 * .:.:.:. Created by @thgMatajs on 17/11/19 .:.:.:.
 */
object GetLatLong {

    fun byPlace(context: Context, place: String): CurrentLocation {
        return try {
            val geoCoder: Address = Geocoder(context)
                .getFromLocationName(place, 1).first()
            loggerSuccess("GEO_CODER::: byPlace>>> $geoCoder")
            CurrentLocation(
                latitude = geoCoder.latitude,
                longitude = geoCoder.longitude
            )
        } catch (ex: Exception) {
            loggerError("GEO_CODER::: Error>>> ${ex.message}")
            CurrentLocation(0.0,0.0)
        }
    }
}