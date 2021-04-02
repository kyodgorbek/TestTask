package com.example.testtask.root.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater

fun Context.inflater(): LayoutInflater = LayoutInflater.from(this)


fun Context.createIntentToApplicationPackage(): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    return intent
}


infix fun Context.callTo(number:String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: $number"))
    startActivity(intent)
}