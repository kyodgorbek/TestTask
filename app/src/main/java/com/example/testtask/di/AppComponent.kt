package com.example.testtask.di

import com.example.testtask.Application
import com.example.testtask.data.di.NetworkModule
import com.example.testtask.data.di.NetworkModuleBinds
import com.example.testtask.domain.di.DomainModuleRepositoryBinds
import com.example.testtask.root.di.RootModule
import com.example.testtask.root.di.RootModuleBinds
import com.example.testtask.ui.screens.main.di.MainComponent
import dagger.Component

@Component(
    modules = [
        RootModule::class,
        RootModuleBinds::class,
        NetworkModule::class,
        NetworkModuleBinds::class,
        DomainModuleRepositoryBinds::class,
    ]
)
interface AppComponent {

	@Component.Builder
	interface Builder {

		fun withNetworkModule(networkModule : NetworkModule) : Builder
		fun withRootModule(rootModule : RootModule) : Builder
		fun build() : AppComponent
	}

	val mainComponent : MainComponent.Builder

	fun inject(target : Application)
}