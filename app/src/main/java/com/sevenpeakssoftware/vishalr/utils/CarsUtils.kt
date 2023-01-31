package com.sevenpeakssoftware.vishalr.utils

import android.content.Context
import android.net.ConnectivityManager

object CarsUtils {
    // Check internet is available or not
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //null check is for airplane mode
        return netInfo != null && netInfo.isConnected
    }
}