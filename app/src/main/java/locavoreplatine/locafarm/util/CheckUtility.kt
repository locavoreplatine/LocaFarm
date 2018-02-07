package locavoreplatine.locafarm.util

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

/**
 * Created by aoudia on 07/02/18.
 */


object CheckUtility : AnkoLogger {



    /**
     * To check device has internet
     *
     * @param context
     * @return boolean as per status
     */
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }


    fun isWifiNetworkConnected(context: Context): Boolean {
        var wifiNetworkState = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo

        if (netInfo.type == ConnectivityManager.TYPE_WIFI ){
            wifiNetworkState = true
        }

        return netInfo != null && wifiNetworkState
    }

    fun checkCoarseLocationPermission(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    fun checkFineLocationPermission(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    fun canGetLocation(context: Context): Boolean {
        var gpsEnabled = false
        var networkEnabled = false

        val lm = context.getSystemService(LOCATION_SERVICE) as LocationManager

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {

        }
        return !(!gpsEnabled && !networkEnabled)
    }

    fun requestLocation(context: Context) {
        if (!canGetLocation(context)) {
            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { _, _ ->
                context.toast("You haven't enabled your GPS")
            }
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
            alertDialog.show()
        }
    }




}