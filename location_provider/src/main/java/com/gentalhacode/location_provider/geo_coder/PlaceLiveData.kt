package com.gentalhacode.location_provider.geo_coder

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import com.gentalhacode.location_provider.logger.loggerError
import com.gentalhacode.location_provider.logger.loggerSuccess
import com.gentalhacode.location_provider.model.Place

/**
 * .:.:.:. Created by @thgMatajs on 16/11/19 .:.:.:.
 */
class PlaceLiveData(
    private val context: Context,
    private val latitude: Double,
    private val longitude: Double
) : LiveData<Place>() {

    override fun onActive() {
        super.onActive()
        getGeoCoder()
    }

    private fun getGeoCoder() {
        try {
            val geoCoder: Address = Geocoder(context)
                .getFromLocation(latitude, longitude, 1).first()
            val currentPlace = geoCoder.run {
                Place(
                    stateName = adminArea,
                    cityName = subAdminArea,
                    countryName = countryName,
                    district = subLocality,
                    address = thoroughfare,
                    number = subThoroughfare,
                    cep = postalCode,
                    fullAddress = getAddressLine(0)
                )
            }
            postValue(currentPlace)
            loggerSuccess("GEO_CODER:::ADMIN_AREA >>> ${geoCoder.adminArea}") //Estado
            loggerSuccess("GEO_CODER:::SUB_ADMIN_AREA >>> ${geoCoder.subAdminArea}")//Cidade
            loggerSuccess("GEO_CODER:::countryName >>> ${geoCoder.countryName}")//Pais
            loggerSuccess("GEO_CODER:::subLocality >>> ${geoCoder.subLocality}")//Bairro
            loggerSuccess("GEO_CODER:::postalCode >>> ${geoCoder.postalCode}")//CEP
            loggerSuccess("GEO_CODER:::subThoroughfare >>> ${geoCoder.subThoroughfare}")//Numero
            loggerSuccess("GEO_CODER:::thoroughfare >>> ${geoCoder.thoroughfare}")//Endereço
            loggerSuccess("GEO_CODER:::AddressLine >>> ${geoCoder.getAddressLine(0)}")//FullAddress
            loggerSuccess("GEO_CODER:::$geoCoder")

        } catch (ex: Exception) {
            getGeoCoder()
            loggerError(ex.message ?: "")
        }
    }
}