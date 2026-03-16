package com.example.watermarkcamera.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

/**
 * GPS 位置获取工具
 * 优先使用 FusedLocationProvider，降级到系统 LocationManager
 */
class LocationHelper(private val context: Context) {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    private var lastLocation: Location? = null
    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun startUpdates() {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(2000L)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                lastLocation = result.lastLocation
            }
        }

        fusedClient.requestLocationUpdates(request, locationCallback!!, Looper.getMainLooper())

        // 同时尝试获取最后已知位置作为初始值
        fusedClient.lastLocation.addOnSuccessListener { location ->
            if (location != null && lastLocation == null) {
                lastLocation = location
            }
        }
    }

    fun stopUpdates() {
        locationCallback?.let { fusedClient.removeLocationUpdates(it) }
        locationCallback = null
    }

    fun getLastLocation(): Location? = lastLocation

    /**
     * 格式化为水印显示文字，如 "31.2304°N, 121.4737°E"
     */
    fun formatForWatermark(location: Location): String {
        val lat = location.latitude
        val lon = location.longitude
        val latDir = if (lat >= 0) "N" else "S"
        val lonDir = if (lon >= 0) "E" else "W"
        return "%.4f°%s, %.4f°%s".format(Math.abs(lat), latDir, Math.abs(lon), lonDir)
    }

    /**
     * 转换为 EXIF 所需的度分秒格式
     */
    fun toExifDMS(decimal: Double): String {
        val abs = Math.abs(decimal)
        val degrees = abs.toInt()
        val minutesDouble = (abs - degrees) * 60
        val minutes = minutesDouble.toInt()
        val seconds = ((minutesDouble - minutes) * 60 * 1000).toInt()
        return "$degrees/1,$minutes/1,$seconds/1000"
    }
}
