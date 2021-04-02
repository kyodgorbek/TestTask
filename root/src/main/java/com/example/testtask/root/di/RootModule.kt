package com.example.testtask.root.di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
import dagger.Module
import dagger.Provides

@Module
class RootModule(private val application: Application) {

    @SuppressLint("HardwareIds")
    @Provides
    @DeviceID
    fun providesDeviceId(@ApplicationContext context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @Provides
    fun providesApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    fun providesApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}