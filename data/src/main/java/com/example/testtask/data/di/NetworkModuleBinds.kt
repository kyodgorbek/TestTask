package com.example.testtask.data.di

import com.example.testtask.data.root.NetworkHelper
import com.example.testtask.data.root.NetworkHelperImpl
import com.example.testtask.data.root.ResultFactory
import com.example.testtask.data.root.ResultFactoryImpl
import com.example.testtask.root.di.RootModule
import com.example.testtask.root.di.RootModuleBinds
import dagger.Binds
import dagger.Module

@Module(includes = [RootModule::class,RootModuleBinds::class])
interface NetworkModuleBinds {

    @Binds
    fun providesNetworkHelper(helperImpl: NetworkHelperImpl): NetworkHelper

    @Binds
    fun providesResultFactory(resultFactoryImpl: ResultFactoryImpl):ResultFactory

}
