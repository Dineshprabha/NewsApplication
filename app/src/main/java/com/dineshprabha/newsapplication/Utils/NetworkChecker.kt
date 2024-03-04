package com.dineshprabha.newsapplication.Utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

class NetworkChecker(val connectivityManager: ConnectivityManager) {

    //checking the network availability
    @RequiresApi(Build.VERSION_CODES.M)
    fun performAction(action : () -> Unit){
        if (hasValidInternetConnection()){
            action()
        }
    }

    //Verify the network type to fetch data from api
    @RequiresApi(Build.VERSION_CODES.M)
    fun hasValidInternetConnection(): Boolean {
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

    }
}