package com.example.testtask.domain.di

import com.example.testtask.data.di.NetworkModule
import com.example.testtask.data.di.NetworkModuleBinds
import com.example.testtask.data.repository.routes.TestRemoteRepository
import com.example.testtask.data.repository.routes.TestRemoteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, NetworkModuleBinds::class])
abstract class DomainModuleRepositoryBinds {

	@Binds
	abstract fun bindsTestRemoteRepository(testRemoteRepositoryImpl : TestRemoteRepositoryImpl): TestRemoteRepository
}