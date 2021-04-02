package com.example.testtask.root.utils

interface SharedPreferencesHelper {

	fun saveAuthToken(data : String?)
	fun getAuthToken() : String?
	fun saveDeviceId(data : String)
	fun getDeviceId() : String?
	fun saveUserInfo(jsonInfo:String)
	fun getUserInfo() : String?
	fun saveFirebaseToken(data : String?)
	fun getFirebaseToken() : String?
}