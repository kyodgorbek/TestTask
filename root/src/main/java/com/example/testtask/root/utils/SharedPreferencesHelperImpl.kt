package com.example.testtask.root.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.testtask.root.di.ApplicationContext
import javax.inject.Inject

class SharedPreferencesHelperImpl
@Inject
constructor(
    @ApplicationContext private val context : Context
) : SharedPreferencesHelper {

	companion object {

		private const val TOKEN = "Token"
		private const val FIREBASE_TOKEN = "FirebaseToken"
		private const val DEVICE_ID = "DeviceID"
		private const val USER_INFO = "UserInfo"
	}

	private val preferences : SharedPreferences =
		context.getSharedPreferences("Configs", MODE_PRIVATE)

	override fun saveAuthToken(data : String?) {
		preferences.edit(true) { putString(TOKEN, data) }
	}

	override fun getAuthToken() : String? {
		return preferences.getString(TOKEN, null)
	}

	override fun saveDeviceId(data : String) {
		preferences.edit(true) {
			putString(DEVICE_ID, data)
		}
	}

	override fun getDeviceId() : String? {
		return preferences.getString(DEVICE_ID, null)
	}

	override fun saveUserInfo(jsonInfo : String) {
		preferences.edit(true){
			putString(USER_INFO,jsonInfo)
		}
	}

	override fun getUserInfo() : String? {
		return preferences.getString(USER_INFO,null)
	}

	override fun saveFirebaseToken(data : String?) {
		preferences.edit(true) {
			putString(FIREBASE_TOKEN, data)
		}
	}

	override fun getFirebaseToken() : String? {
		return preferences.getString(FIREBASE_TOKEN, null)
	}

}