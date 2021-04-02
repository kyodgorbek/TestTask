package com.example.testtask.root.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.example.testtask.root.di.ApplicationContext
import javax.inject.Inject

class NetworkConnectivityCheckerImpl
@Inject
constructor(
    @ApplicationContext private val context: Context
) : NetworkConnectivityChecker {

    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override val isNetworkAvailable: Boolean
        @SuppressLint("MissingPermission")
        get() = connectivityManager.activeNetworkInfo?.isConnected == true

}