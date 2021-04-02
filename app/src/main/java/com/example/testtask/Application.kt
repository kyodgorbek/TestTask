package com.example.testtask

import android.app.Application
import com.example.testtask.di.AppComponent
import com.example.testtask.di.DependencyInjectionStorage
import com.example.testtask.root.di.DeviceID
import com.example.testtask.root.utils.SharedPreferencesHelper
import javax.inject.Inject

class Application : Application() {

	companion object {

		private lateinit var mInstance : com.example.testtask.Application

		fun getInstance() = mInstance
	}

	lateinit var injectionStorage : DependencyInjectionStorage

	@Inject
	lateinit var mShared : SharedPreferencesHelper

	@Inject
	@DeviceID
	lateinit var deviceId : String

	override fun onCreate() {
		super.onCreate()
		mInstance = this
		injectionStorage = DependencyInjectionStorage.initialize(this)

		injectionStorage.get<AppComponent>().inject(this)

		mShared.saveDeviceId(deviceId)
	}

}