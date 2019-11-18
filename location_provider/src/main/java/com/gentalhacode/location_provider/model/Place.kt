package com.gentalhacode.location_provider.model

/**
 * .:.:.:. Created by @thgMatajs on 16/11/19 .:.:.:.
 */
data class Place(
    val stateName: String,
    val cityName: String,
    val countryName: String,
    val district: String,
    val address: String,
    val number: String,
    val cep: String,
    val fullAddress: String
)