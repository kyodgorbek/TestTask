package com.example.testtask.mvvm.ui

import androidx.annotation.StringRes

interface IBaseView {

    fun showServerError(message:String)
    fun showServerError(@StringRes resId: Int)
    fun showNetworkErrorSnackBar(message: String)
    fun showNetworkErrorSnackBar(@StringRes resId: Int)
    fun showToast(message: String)
    fun showToast(@StringRes resId: Int)

    fun hasPermission(permission:String):Boolean

}