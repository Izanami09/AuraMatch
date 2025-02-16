package com.example.auraprototype.domain.sc

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

fun hasNetwork(context: Context) : Boolean{
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val actnetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        actnetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actnetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actnetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        actnetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }
}
