package com.example.testtask.ui.screens.splash.di

import com.example.testtask.root.di.RootModuleBinds
import com.example.testtask.ui.screens.splash.SplashScreenSecondActivity
import dagger.Component

@Component(modules = [RootModuleBinds::class])
interface SplashScreenComponent {
	fun inject(target: SplashScreenSecondActivity)
}