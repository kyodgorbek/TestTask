package com.example.testtask.root.di

import com.example.testtask.root.utils.NetworkConnectivityChecker
import com.example.testtask.root.utils.NetworkConnectivityCheckerImpl
import com.example.testtask.root.utils.SharedPreferencesHelper
import com.example.testtask.root.utils.SharedPreferencesHelperImpl
import dagger.Binds
import dagger.Module

@Module(includes = [RootModule::class])
abstract class RootModuleBinds {

    @Binds
    abstract fun provideSharedPreferences(sharedPreferencesHelperImpl: SharedPreferencesHelperImpl): SharedPreferencesHelper

    @Binds
    abstract fun bindsNetworkConnectivityChecker(checkerImpl: NetworkConnectivityCheckerImpl): NetworkConnectivityChecker

}