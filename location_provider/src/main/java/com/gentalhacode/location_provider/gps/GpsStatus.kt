package com.gentalhacode.location_provider.gps

/**
 * .:.:.:. Created by @thgMatajs on 16/11/19 .:.:.:.
 */
sealed class GpsStatus {
    data class Enabled(val message: String = "GPS IS ENABLED"): GpsStatus()
    data class Disabled(val message: String = "GPS IS OFF"): GpsStatus()
    data class Failed(val message: String = "SOMETHING WENT WRONG"): GpsStatus()
}